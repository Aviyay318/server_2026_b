package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.EmployeeConstraintsRepository;
import server_2026_b.server.database.EmployeeRepository;
import server_2026_b.server.database.EmployerSettingsRepository;
import server_2026_b.server.database.ShiftRepository;
import server_2026_b.server.entities.EmployeeConstraint;
import server_2026_b.server.entities.EmployerSettings;
import server_2026_b.server.entities.Shift;
import server_2026_b.server.entities.User;
import server_2026_b.server.requests.EmployeeConstraintRequest;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.EmployeeConstraintResponse;
import server_2026_b.server.responses.EmployeeConstraintsResponse;
import server_2026_b.server.responses.SettingsResponse;
import server_2026_b.server.responses.ShiftListResponse;
import server_2026_b.server.utils.Errors;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeConstraintsService {

    private final EmployeeConstraintsRepository employeeConstraintsRepository;
    private final UserService userService;
    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;
    private final EmployerSettingsRepository employerSettingsRepository;

    public EmployeeConstraintsService(EmployerSettingsRepository employerSettingsRepository, EmployeeConstraintsRepository employeeConstraintsRepository, UserService userService, EmployeeRepository employeeRepository, ShiftRepository shiftRepository) {
        this.employeeConstraintsRepository = employeeConstraintsRepository;
        this.userService = userService;
        this.employeeRepository = employeeRepository;
        this.shiftRepository = shiftRepository;
        this.employerSettingsRepository = employerSettingsRepository;
    }

    public BasicResponse saveConstraints(String token, EmployeeConstraintRequest request) {
        try {
            User employee = userService.getEmployeeByAccessToken(token);
            if (employee == null) {
                return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
            }
            User employer = employeeRepository.findEmployerForEmployee(employee.getId());
            if (employer == null) {
                return new BasicResponse(false, Errors.ERROR_NO_EMPLOYER_FOUND);
            }
            if (request == null) {
                return new BasicResponse(false, Errors.ERROR_EMPTY_CONSTRAINTS);
            }

            // Fix #7: guard against missing or unconfigured employer settings (NPE prevention).
            // Policy: block submission if the employer hasn't set an expiration date yet.
            EmployerSettings employerSettings = employerSettingsRepository.findByEmployerId(employer.getId());
            if (employerSettings == null || employerSettings.getSubmissionExpiration() == null) {
                return new BasicResponse(false, Errors.ERROR_CONSTRAINTS_SUBMITTED_AFTER_EXPIRATION);
            }
            if (employerSettings.getSubmissionExpiration().isBefore(LocalDateTime.now())) {
                return new BasicResponse(false, Errors.ERROR_CONSTRAINTS_SUBMITTED_AFTER_EXPIRATION);
            }

            List<EmployeeConstraint> employeeConstraintList = new ArrayList<>();
            Timestamp constraintTimestamp = Timestamp.valueOf(request.getDate());
            for (EmployeeConstraintRequest.Constraint constraint : request.getConstrains()) {
                // הגבלת אילוצים - אם קיים כבר אילוץ עבור אותו עובד באותה משמרת באותו היום, לא ניתן לו להוסיף
                if (employeeConstraintsRepository.constraintExists(
                        employee.getPersonalId(),
                        constraint.getShiftId(),
                        constraintTimestamp)) {
                    return new BasicResponse(false, Errors.ERROR_CONSTRAINT_ALREADY_EXISTS);
                }
                Shift shift = shiftRepository.findById(constraint.getShiftId());
                if (shift == null) {
                    return new BasicResponse(false, Errors.ERROR_SHIFT_NOT_FOUND);
                }
                employeeConstraintList.add(new EmployeeConstraint(
                        employee,
                        shift,
                        constraint.isAvailable(),
                        constraint.getComment(),
                        Timestamp.valueOf(request.getDate())
                ));
            }
            employeeConstraintsRepository.saveList(employeeConstraintList);
            return new BasicResponse(true, null);

        } catch (Exception e) {
            System.err.println(e);
            return new BasicResponse(false, Errors.ERROR_SAVE_CONSTRAINTS_FAILED);
        }
    }

    public EmployeeConstraintsResponse getAllConstraints(String token) {
        try {
            User employer = userService.getEmployerByAccessToken(token);
            if (employer == null) {
                return new EmployeeConstraintsResponse(false, Errors.ERROR_INVALID_TOKEN, null);
            }

            List<EmployeeConstraint> dbConstraints = employeeConstraintsRepository.getAllConstraints(); // כל האילוצים

            List<EmployeeConstraintResponse> constraints = dbConstraints.stream()
                    .map(constraint -> new EmployeeConstraintResponse(
                            constraint.getEmployee() != null ? constraint.getEmployee().getPersonalId() : null,
                            constraint.getShift() != null ? constraint.getShift().getId() : 0L,
                            constraint.isAvailable(),
                            constraint.getComment(),
                            constraint.getDate()
                    ))
                    .toList();

            return new EmployeeConstraintsResponse(true, null, constraints);

        } catch (Exception e) {
            // Pro tip: Print or log your stack trace here so you don't swallow errors blindly!
            e.printStackTrace();
            return new EmployeeConstraintsResponse(
                    false,
                    Errors.ERROR_GET_CONSTRAINTS_FAILED,
                    null
            );
        }
    }

    // Fix #3: new method — lets an employee read their employer's submissionExpiration.
    // Security: only authenticated employees can call this; returns only the expiration date.
    public SettingsResponse getSettingsForEmployee(String token) {
        User employee = userService.getEmployeeByAccessToken(token);
        if (employee == null) {
            return new SettingsResponse(false, Errors.ERROR_INVALID_TOKEN);
        }
        User employer = employeeRepository.findEmployerForEmployee(employee.getId());
        if (employer == null) {
            return new SettingsResponse(false, Errors.ERROR_NO_EMPLOYER_FOUND);
        }
        EmployerSettings settings = employerSettingsRepository.findByEmployerId(employer.getId());
        // Return null expiration gracefully — client interprets null as "not configured"
        if (settings == null) {
            return new SettingsResponse(true, (java.time.LocalDateTime) null);
        }
        return new SettingsResponse(true, settings.getSubmissionExpiration());
    }

    // Fix #4: new method — lets an employee fetch their employer's published (active + posted) shifts.
    // Security: employee can only see shifts belonging to their own employer.
    public ShiftListResponse getPublishedShiftsForEmployee(String token) {
        User employee = userService.getEmployeeByAccessToken(token);
        if (employee == null) {
            return new ShiftListResponse(false, Errors.ERROR_INVALID_TOKEN, null);
        }
        User employer = employeeRepository.findEmployerForEmployee(employee.getId());
        if (employer == null) {
            return new ShiftListResponse(false, Errors.ERROR_NO_EMPLOYER_FOUND, null);
        }
        // findActiveByEmployerId already filters active=true; we additionally filter posted=true
        List<Shift> publishedShifts = shiftRepository.findActiveByEmployerId(employer.getId())
                .stream()
                .filter(shift -> Boolean.TRUE.equals(shift.getPosted()))
                .toList();
        return new ShiftListResponse(true, null, publishedShifts);
    }
}