package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.ShiftRepository;
import server_2026_b.server.entities.Shift;
import server_2026_b.server.entities.User;
import server_2026_b.server.requests.ShiftListRequest;
import server_2026_b.server.requests.ShiftRequest;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.ShiftListResponse;
import server_2026_b.server.utils.Errors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShiftService {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final int FIRST_WEEK_DAY = 1;
    private static final int LAST_WEEK_DAY = 7;
    private final ShiftRepository shiftRepository;
    private final UserService userService;
    public ShiftService(ShiftRepository shiftRepository, UserService userService) {
        this.shiftRepository = shiftRepository;
        this.userService = userService;
    }

    public BasicResponse setShifts(String accessToken, ShiftListRequest request) {
        User employer = getEmployerOrNull(accessToken);
        if (employer == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }
        if (request == null || request.getShifts() == null || request.getShifts().isEmpty()) {
            return new BasicResponse(false, Errors.ERROR_EMPTY_FIELD);
        }
        List<Shift> shifts = new ArrayList<>();
        for (ShiftRequest shiftRequest : request.getShifts()) {
            Shift shift = createShiftFromRequest(employer.getId(), shiftRequest, false);
            if (shift == null) {
                return new BasicResponse(false, Errors.ERROR_EMPTY_FIELD);
            }
            shifts.add(shift);
        }
        shiftRepository.saveAll(shifts);
        return new BasicResponse(true, null);
    }

    public ShiftListResponse getShifts(String accessToken) {
        User employer = getEmployerOrNull(accessToken);
        if (employer == null) {
            return new ShiftListResponse(false, Errors.ERROR_INVALID_TOKEN, null);
        }
        List<Shift> shifts = shiftRepository.findAllByEmployerId(employer.getId());
        return new ShiftListResponse(true, null, shifts);
    }

    public ShiftListResponse postShifts(String accessToken) {
        User employer = getEmployerOrNull(accessToken);
        if (employer == null) {
            return new ShiftListResponse(false, Errors.ERROR_INVALID_TOKEN, null);
        }
        List<Shift> shifts = shiftRepository.findAllByEmployerId(employer.getId());
        for (Shift shift : shifts) {
            shift.setActive(true);
            shiftRepository.save(shift);
        }
        List<Shift> activeShifts = shiftRepository.findActiveByEmployerId(employer.getId());
        return new ShiftListResponse(true, null, activeShifts);
    }

    public BasicResponse updateShift(String accessToken, ShiftRequest request) {
        User employer = getEmployerOrNull(accessToken);
        if (employer == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }
        if (request == null || request.getId() == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_ID);
        }
        Shift oldShift = shiftRepository.findById(request.getId());
        if (oldShift == null) {
            return new BasicResponse(false, Errors.ERROR_SHIFT_NOT_FOUND);
        }
        if (!isShiftBelongsToEmployer(oldShift, employer)) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }
        Shift newShift = createShiftFromRequest(employer.getId(), request, false);
        if (newShift == null) {
            return new BasicResponse(false, Errors.ERROR_EMPTY_FIELD);
        }
        oldShift.setActive(false);
        shiftRepository.save(oldShift);
        shiftRepository.save(newShift);
        return new BasicResponse(true, null);
    }

    public BasicResponse deleteShift(String accessToken, Long shiftId) {
        User employer = getEmployerOrNull(accessToken);
        if (employer == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }

        if (shiftId == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_ID);
        }
        Shift shift = shiftRepository.findById(shiftId);
        if (shift == null) {
            return new BasicResponse(false, Errors.ERROR_SHIFT_NOT_FOUND);
        }
        if (!isShiftBelongsToEmployer(shift, employer)) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }
        shift.setActive(false);
        shiftRepository.save(shift);
        return new BasicResponse(true, null);
    }

    private User getEmployerOrNull(String accessToken) {
        return userService.getEmployerByAccessToken(accessToken);
    }

    private boolean isShiftBelongsToEmployer(Shift shift, User employer) {
        return shift != null
                && employer != null
                && employer.getId() != null
                && employer.getId().equals(shift.getEmployerId());
    }

    private Shift createShiftFromRequest(Long employerId, ShiftRequest request, Boolean active) {
        if (hasMissingShiftFields(employerId, request)) {
            return null;
        }
        if (!isValidWeekDay(request.getWeekDay())) {
            return null;
        }
        if (!isValidEmployeeAmount(request.getEmployeeAmount())) {
            return null;
        }
        try {
            Date startTime = parseDate(request.getStartTime());
            Date endTime = parseDate(request.getEndTime());
            if (!endTime.after(startTime)) {
                return null;
            }

            return new Shift(null, employerId, active,
                    request.getWeekDay(), startTime, endTime,
                    request.getEmployeeAmount()
            );
        } catch (ParseException e) {
            return null;
        }
    }

    private boolean hasMissingShiftFields(Long employerId, ShiftRequest request) {
        return employerId == null || request == null || request.getWeekDay() == null || isBlank(request.getStartTime())
                || isBlank(request.getEndTime()) || request.getEmployeeAmount() == null;
    }

    private boolean isValidWeekDay(Integer weekDay) {
        return weekDay != null
                && weekDay >= FIRST_WEEK_DAY
                && weekDay <= LAST_WEEK_DAY;
    }

    private boolean isValidEmployeeAmount(Integer employeeAmount) {
        return employeeAmount != null && employeeAmount > 0;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private Date parseDate(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        simpleDateFormat.setLenient(false);
        return simpleDateFormat.parse(date.trim());
    }
}
