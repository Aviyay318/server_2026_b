package server_2026_b.server.service;
import server_2026_b.server.database.ShiftRepository;
import server_2026_b.server.entities.Shift;
import server_2026_b.server.entities.Employee;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import server_2026_b.server.responses.ShiftResponse;
import server_2026_b.server.utils.Errors;

@Service
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final UserService userService;

    public ShiftService(ShiftRepository shiftRepository, UserService userService) {
        this.shiftRepository = shiftRepository;
        this.userService = userService;
    }

    public ShiftResponse getWeeklyShifts(String accessToken,String fromDate, String toDate) {
        Employee employee = userService.getEmployeeByAccessToken(accessToken);
        if(employee == null){
            return new ShiftResponse(false, Errors.ERROR_INVALID_TOKEN, null);
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date from = sdf.parse(fromDate);
            Date to = sdf.parse(toDate);
            List<Shift> shifts = shiftRepository.findShiftsByUserId(employee.getId(), from, to);
            return new ShiftResponse(true, null, shifts);
        }catch (java.text.ParseException e){
            return new ShiftResponse(false, Errors.ERROR_INVALID_DATE_FORMAT, null);
        }catch (Exception e){
            return new ShiftResponse(false, Errors.ERROR_FETCHING_SHIFTS, null);
        }
    }
}
