package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.WorkShiftRepository;
import server_2026_b.server.entities.User;
import server_2026_b.server.entities.WorkShift;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.WorkShiftResponse;
import server_2026_b.server.utils.Errors;
import server_2026_b.server.utils.UserType;

import java.util.List;

@Service
public class WorkShiftService {

    private final WorkShiftRepository workShiftRepository;
    private final UserService userService;

    public WorkShiftService(WorkShiftRepository workShiftRepository, UserService userService) {
        this.workShiftRepository = workShiftRepository;
        this.userService = userService;
    }

    public BasicResponse savePlacements(String token, List<WorkShift> placements) {
        User user = this.userService.getEmployeeByAccessToken(token);
        if (user == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }

        // only managers can save placements, not employees
        if (user.getUserType() == UserType.EMPLOYEE) {
            return new BasicResponse(false, Errors.ERROR_UNAUTHORIZED_ACTION);
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

    public BasicResponse updatePlacement(String token, WorkShift placement) {
        User user = this.userService.getEmployeeByAccessToken(token);
        if (user == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }

        // only managers can update placements, not employees
        if (user.getUserType() == UserType.EMPLOYEE) {
            return new BasicResponse(false, Errors.ERROR_UNAUTHORIZED_ACTION);
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

    public WorkShiftResponse getAllPlacements(String token) {
        User user = this.userService.getEmployeeByAccessToken(token);
        if (user == null) {
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
    //נותנת את הסידור עבודה המלא למנהלים אבל מגבילה עובדים לצפייה בלוז שלהם 
public WorkShiftResponse getPlacementsForEmployee(String token, Long employeeId) {
    User user = this.userService.getEmployeeByAccessToken(token);
    if (user == null) {
        return new WorkShiftResponse(false, Errors.ERROR_INVALID_TOKEN, null);
    }
    if (employeeId == null) {
        return new WorkShiftResponse(false, Errors.ERROR_INVALID_WORK_SHIFT_PLACEMENT, null);
    }
    if (user.getUserType() == UserType.EMPLOYEE && !user.getId().equals(employeeId)) {
        return new WorkShiftResponse(false, Errors.ERROR_UNAUTHORIZED_ACTION, null);
    }
    try {
        List<WorkShift> list = workShiftRepository.getByEmployeeId(employeeId);
        return new WorkShiftResponse(true, null, list);
    } catch (Exception e) {
        e.printStackTrace();
        return new WorkShiftResponse(false, Errors.ERROR_FETCHING_WORKSHIFTS, null);
    }
}
}
