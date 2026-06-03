package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.EmployeeConstraintsRepository;
import server_2026_b.server.entities.EmployeeConstraint;
import server_2026_b.server.entities.User;
import server_2026_b.server.requests.EmployeeConstraintRequest;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.EmployeeConstraintResponse;
import server_2026_b.server.responses.EmployeeConstraintsResponse;
import server_2026_b.server.utils.Errors;

import java.util.List;

@Service
public class EmployeeConstraintsService {

    private final EmployeeConstraintsRepository employeeConstraintsRepository;
    private final UserService userService;

    public EmployeeConstraintsService(EmployeeConstraintsRepository employeeConstraintsRepository, UserService userService) {
        this.employeeConstraintsRepository = employeeConstraintsRepository;
        this.userService = userService;
    }

    public BasicResponse saveConstraints(String token, List<EmployeeConstraintRequest> requests) { // הוספת רשימת אילוצים של עובד כאילוצים בטבלה
        try {
            User employee = userService.getEmployeeByAccessToken(token);
            if (employee == null) {
                return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
            }
            if (requests == null || requests.isEmpty()) {
                return new BasicResponse(false, Errors.ERROR_EMPTY_CONSTRAINTS);
            }
            for (EmployeeConstraintRequest request : requests) {
                //  מוודאים שהת״ז שמגיע מהלקוח ב-request תקין
                if (!request.getEmployeeId().equals(employee.getPersonalId())) {
                    return new BasicResponse(false, Errors.ERROR_INVALID_ID);
                }
                // הגבלת אילוצים - אם קיים כבר אילוץ עבור אותו עובד באותה משמרת באותו היום, לא ניתן לו להוסיף
                if (employeeConstraintsRepository.constraintExists(
                        request.getEmployeeId(),
                        request.getShiftId(),
                        request.getDate())) {
                    return new BasicResponse(false, Errors.ERROR_CONSTRAINT_ALREADY_EXISTS);
                }
            }
            // עבור כל אובייקט מהרשימה שהגיעה מהלקוח ניצור אובייקט אילוץ ונכניס אותו לטבלה
            for (EmployeeConstraintRequest request : requests) {
                EmployeeConstraint constraint = new EmployeeConstraint(
                        request.getEmployeeId(),
                        request.getShiftId(),
                        request.isAvailable(),
                        request.getComment(),
                        request.getDate()
                );
                employeeConstraintsRepository.createConstraint(constraint);
            }
            return new BasicResponse(true, null);

        } catch (Exception e) {
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

            // כדי לא למחוק את ה-id ב-EmployeeConstraint נחזיר בתגובה הסופית רשימה מסוג האובייקט שבתוך EmployeeConstraintResponse (האילוץ בלי auto id)
            List<EmployeeConstraintResponse> constraints = dbConstraints.stream()
                    .map(constraint -> new EmployeeConstraintResponse(
                            constraint.getEmployeeId(),
                            constraint.getShiftId(),
                            constraint.isAvailable(),
                            constraint.getComment(),
                            constraint.getDate()
                    ))
                    .toList();

            return new EmployeeConstraintsResponse(true, null, constraints);

        } catch (Exception e) {
            return new EmployeeConstraintsResponse(
                    false,
                    Errors.ERROR_GET_CONSTRAINTS_FAILED,
                    null
            );
        }
    }
}