package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.WorkShiftRepository;
import server_2026_b.server.entities.User;
import server_2026_b.server.entities.WorkShift;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.WorkShiftResponse;
import server_2026_b.server.utils.Errors;

import java.util.List;

@Service
public class WorkShiftService {

    private final WorkShiftRepository workShiftRepository;
    private final UserService userService;

    public WorkShiftService(WorkShiftRepository workShiftRepository, UserService userService) {
        this.workShiftRepository = workShiftRepository;
        this.userService = userService;
    }

    // Fix #6: was incorrectly calling getEmployeeByAccessToken then rejecting employees.
    // Now correctly resolves the employer from the token directly.
    public BasicResponse savePlacements(String token, List<WorkShift> placements) {
        User employer = userService.getEmployerByAccessToken(token);
        if (employer == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }

        if (placements == null || placements.isEmpty()) {
            return new BasicResponse(false, Errors.ERROR_INVALID_WORK_SHIFT_PLACEMENT);
        }

        try {
            for (WorkShift placement : placements) {
                workShiftRepository.save(placement);
            }
            return new BasicResponse(true, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse(false, Errors.ERROR_INVALID_WORK_SHIFT_PLACEMENT);
        }
    }

    // Fix #6: same correction — employer action must use getEmployerByAccessToken.
    public BasicResponse updatePlacement(String token, WorkShift placement) {
        User employer = userService.getEmployerByAccessToken(token);
        if (employer == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }

        if (placement == null || placement.getId() == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_WORK_SHIFT_PLACEMENT);
        }

        try {
            workShiftRepository.save(placement);
            return new BasicResponse(true, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse(false, Errors.ERROR_INVALID_WORK_SHIFT_PLACEMENT);
        }
    }

    // Fix #6: same correction — only employers should retrieve all placements.
    public WorkShiftResponse getAllPlacements(String token) {
        User employer = userService.getEmployerByAccessToken(token);
        if (employer == null) {
            return new WorkShiftResponse(false, Errors.ERROR_INVALID_TOKEN, null);
        }

        try {
            List<WorkShift> list = workShiftRepository.getAll();
            return new WorkShiftResponse(true, null, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new WorkShiftResponse(false, Errors.ERROR_FETCHING_WORKSHIFTS, null);
        }
    }

    // Fix #5: employeeId is now optional. If omitted, it is resolved from the token.
    // If provided, it must match the token's employee to prevent spoofing.
    // נותנת את הסידור עבודה המלא לעובד לפי הטוקן שלו, או לפי employeeId אם נשלח (מוגן מפני spoofing)
    public WorkShiftResponse getPlacementsForEmployee(String token, Long employeeId) {
        User employee = userService.getEmployeeByAccessToken(token);
        if (employee == null) {
            return new WorkShiftResponse(false, Errors.ERROR_INVALID_TOKEN, null);
        }

        // If employeeId was provided, verify it matches the authenticated employee (anti-spoofing)
        if (employeeId != null && !employee.getId().equals(employeeId)) {
            return new WorkShiftResponse(false, Errors.ERROR_UNAUTHORIZED_ACTION, null);
        }

        // Resolve from token when not provided
        Long resolvedId = (employeeId != null) ? employeeId : employee.getId();

        try {
            List<WorkShift> list = workShiftRepository.getByEmployeeId(resolvedId);
            return new WorkShiftResponse(true, null, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new WorkShiftResponse(false, Errors.ERROR_FETCHING_WORKSHIFTS, null);
        }
    }
}
