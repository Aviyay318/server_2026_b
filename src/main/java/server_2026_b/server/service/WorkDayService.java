package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.WorkDayRepository;
import server_2026_b.server.entities.User;
import server_2026_b.server.entities.WorkDay;
import server_2026_b.server.entities.WorkingSite;
import server_2026_b.server.requests.EnterRequest;
import server_2026_b.server.requests.ExitRequest;
import server_2026_b.server.requests.ReportAbsenceRequest;
import server_2026_b.server.responses.AbsenceReasonsResponse;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.SiteListResponse;
import server_2026_b.server.responses.WorkHoursResponse;
import server_2026_b.server.responses.WorkListResponse;
import server_2026_b.server.responses.WorkStatusResponse;
import server_2026_b.server.utils.AbsenceReason;
import server_2026_b.server.utils.Errors;
import server_2026_b.server.utils.ShiftStatus;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Service
public class WorkDayService {
    private final WorkDayRepository workDayRepository;
    private final UserService userService;

    public WorkDayService(WorkDayRepository workDayRepository, UserService userService) {
        this.workDayRepository = workDayRepository;
        this.userService = userService;
    }

    public BasicResponse enter(String token, EnterRequest request) {
        User employee = this.userService.getEmployeeByAccessToken(token);

        if (employee == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }

        WorkDay open = workDayRepository.findOpenByUserId(employee.getId());

        if (open != null) {
            return new BasicResponse(false, Errors.ERROR_EMPLOYEE_ALREADY_WORKING);
        }

        if (request == null || (request.getSiteId() == null && request.getLocation() == null)) {
            return new BasicResponse(false, Errors.ERROR_EMPTY_FIELD);
        }

        WorkingSite site = null;
        if (request.getSiteId() != null) {
            site = workDayRepository.findSiteById(request.getSiteId());
            if (site == null) {
                return new BasicResponse(false, Errors.ERROR_SITE_NOT_FOUND);
            }
        }
        if (site != null && request.getLocation() != null) {
            return new BasicResponse(false, Errors.ERROR_TWO_LOCATIONS_AT_ONCE);
        }

        Timestamp startTime = request.getStartTime() != null
                ? request.getStartTime()
                : new Timestamp(System.currentTimeMillis());

        WorkDay workDay = new WorkDay();
        workDay.setUserId(employee.getId());
        workDay.setEnterTime(startTime);
        workDay.setEnterSite(site);
        workDay.setEnterLocation(request.getLocation());
        workDay.setStatus(ShiftStatus.IN_PROGRESS);

        workDayRepository.save(workDay);

        return new BasicResponse(true, null);
    }

    public BasicResponse exit(String token, ExitRequest request) {
        User employee = this.userService.getEmployeeByAccessToken(token);
        if (employee == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }

        if (request == null) {
            return new BasicResponse(false, Errors.ERROR_EMPTY_FIELD);
        }

        WorkDay open = workDayRepository.findOpenByUserId(employee.getId());

        if (open == null) {
            return new BasicResponse(false, Errors.ERROR_EMPLOYEE_NOT_WORKING);
        }

        WorkingSite site = null;

        if (request.getSiteId() != null) {
            site = workDayRepository.findSiteById(request.getSiteId());
        } else {
            site = null;
        }

        if (site != null && request.getLocation() != null) {
            return new BasicResponse(false, Errors.ERROR_TWO_LOCATIONS_AT_ONCE);
        } else if (site == null && request.getLocation() == null) {
            return new BasicResponse(false, Errors.ERROR_NO_LOCATION_OR_SITE_PROVIDED);
        }

        Timestamp endTime = request.getEndTime() != null
                ? request.getEndTime()
                : new Timestamp(System.currentTimeMillis());

        open.setExitTime(endTime);
        open.setExitSite(site);
        open.setExitLocation(request.getLocation());
        open.setStatus(ShiftStatus.FINISHED);

        workDayRepository.save(open);

        return new BasicResponse(true, null);
    }

    public WorkStatusResponse lastEnter(String token) {
        User employee = this.userService.getEmployeeByAccessToken(token);

        if (employee == null) {
            return new WorkStatusResponse(false, Errors.ERROR_INVALID_TOKEN, false, null);
        }

        WorkDay open = workDayRepository.findOpenByUserId(employee.getId());

        if (open == null) {
            return new WorkStatusResponse(true, Errors.ERROR_EMPLOYEE_NOT_WORKING, false, null);
        }

        return new WorkStatusResponse(true, null, true, open.getEnterTime());
    }

    public WorkListResponse getAllWorkList(String token) {
        User employee = this.userService.getEmployeeByAccessToken(token);

        if (employee == null) {
            return new WorkListResponse(false, Errors.ERROR_INVALID_TOKEN, null);
        }

        List<WorkDay> list = workDayRepository.findAllByUserId(employee.getId());

        return new WorkListResponse(true, null, list);
    }

    public SiteListResponse getAllSites() {
        try {
            List<WorkingSite> sites = workDayRepository.findAllSites();
            return new SiteListResponse(true, null, sites);
        } catch (Exception e) {
            return new SiteListResponse(false, Errors.ERROR_FETCHING_SITES, null);
        }
    }

    public WorkHoursResponse getTotalHoursAtMonth(String token, Integer month) {
        User employee = this.userService.getEmployeeByAccessToken(token);

        if (employee == null) {
            return new WorkHoursResponse(false, Errors.ERROR_INVALID_TOKEN, null, month);
        }

        Double totalHours = workDayRepository.getTotalHoursByMonth(employee.getId(), month);

        return new WorkHoursResponse(true, null, totalHours, month);
    }

    public AbsenceReasonsResponse getAllAbsenceReasons(String token) {
        User employee = this.userService.getEmployeeByAccessToken(token);

        if (employee == null) {
            return new AbsenceReasonsResponse(false, Errors.ERROR_INVALID_TOKEN, null);
        }

        return new AbsenceReasonsResponse(true, null, Arrays.asList(AbsenceReason.values()));
    }

    public BasicResponse reportAbsence(String token, ReportAbsenceRequest request) {
        User employee = this.userService.getEmployeeByAccessToken(token);

        if (employee == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }

        if (request == null || request.getReason() == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_ABSENCE_REASON);
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());

        WorkDay open = workDayRepository.findOpenByUserId(employee.getId());

        if (open != null) {
            open.setStatus(ShiftStatus.ABSENCE);
            open.setAbsenceReason(request.getReason());
            open.setExitTime(now);
            workDayRepository.save(open);
        } else {
            WorkDay workDay = new WorkDay();
            workDay.setUserId(employee.getId());
            workDay.setEnterTime(request.getDate());
            workDay.setStatus(ShiftStatus.ABSENCE);
            workDay.setAbsenceReason(request.getReason());
            workDayRepository.save(workDay);
        }

        return new BasicResponse(true, null);
    }
}
