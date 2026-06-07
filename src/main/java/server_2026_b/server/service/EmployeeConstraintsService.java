package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.EmployeeConstraintsRepository;
import server_2026_b.server.database.EmployeeRepository;
import server_2026_b.server.database.ShiftRepository;
import server_2026_b.server.entities.EmployeeConstraint;
import server_2026_b.server.entities.Shift;
import server_2026_b.server.entities.User;
import server_2026_b.server.requests.EmployeeConstraintRequest;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.EmployeeConstraintResponse;
import server_2026_b.server.responses.EmployeeConstraintsResponse;
import server_2026_b.server.utils.Errors;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeConstraintsService {

    private final EmployeeConstraintsRepository employeeConstraintsRepository;
    private final UserService userService;
    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;

    public EmployeeConstraintsService(EmployeeConstraintsRepository employeeConstraintsRepository, UserService userService, EmployeeRepository employeeRepository, ShiftRepository shiftRepository) {
        this.employeeConstraintsRepository = employeeConstraintsRepository;
        this.userService = userService;
        this.employeeRepository = employeeRepository;
        this.shiftRepository = shiftRepository;
    }

    public BasicResponse saveConstraints(String token, EmployeeConstraintRequest request) { // הוספת רשימת אילוצים של עובד כאילוצים בטבלה
        try {
            User employee = userService.getEmployeeByAccessToken(token);
            if (employee == null) {
                return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
            }
            if (request == null) {
                return new BasicResponse(false, Errors.ERROR_EMPTY_CONSTRAINTS);
            }
            if (employeeRepository.findEmployerForEmployee(employee.getId()) == null){
                return new BasicResponse(false,Errors.ERROR_EMPLOYER_NOT_FOUND);
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
                if (shift == null){
                    return new BasicResponse(false,Errors.ERROR_SHIFT_NOT_FOUND);
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
}