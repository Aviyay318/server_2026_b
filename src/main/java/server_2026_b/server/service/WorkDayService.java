package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;
import server_2026_b.server.database.UserRepository;
import server_2026_b.server.database.WorkDayRepository;
import server_2026_b.server.entities.RefreshToken;
import server_2026_b.server.entities.Employee;
import server_2026_b.server.entities.WorkDay;
import server_2026_b.server.entities.WorkingSite;
import server_2026_b.server.requests.EnterRequest;
import server_2026_b.server.requests.ExitRequest;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.SiteListResponse;
import server_2026_b.server.responses.WorkHoursResponse;
import server_2026_b.server.responses.WorkListResponse;
import server_2026_b.server.responses.WorkStatusResponse;
import server_2026_b.server.utils.Errors;
import server_2026_b.server.utils.UserType;

import java.util.Date;
import java.util.List;

@Service
public class WorkDayService {
    private final TokenService tokenService;
    private final WorkDayRepository workDayRepository;
   private final UserService userService;
   private final Persist persist;

    public WorkDayService(Persist persist, TokenService tokenService, WorkDayRepository workDayRepository, UserService userService) {
       this.persist = persist;
        this.tokenService = tokenService;
        this.workDayRepository = workDayRepository;
        this.userService =userService;
    }

    public Employee getEmployeeByToken(String token){
        return userService.getEmployeeByAccessToken(token);
    }

    public BasicResponse enter(String token, EnterRequest request) {
        Employee employee = getEmployeeByToken(token);
        if(employee == null){
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }
        WorkDay open = workDayRepository.findOpenByUserId(employee.getId());
        if(open != null){
            return new BasicResponse(false, Errors.ERROR_EMPLOYEE_ALREADY_WORKING);
        }
        if (request == null || (request.getSiteId() == null && request.getLocation() == null)){
            return new BasicResponse(false, Errors.ERROR_EMPTY_FIELD);
        }
        WorkingSite site;
        if (request.getSiteId() != null){
            site = persist.loadObject(WorkingSite.class, request.getSiteId());
        } else {
            site = null;
        }
        if(site != null && request.getLocation() != null){
            return new BasicResponse(false , Errors.ERROR_TWO_LOCATIONS_AT_ONCE);
        } else if (site == null && request.getLocation() == null) {
            return new BasicResponse(false , Errors.ERROR_NO_LOCATION_OR_SITE_PROVIDED);
        }
        Date startTime = request.getStartTime() != null? request.getStartTime() : new Date();
        WorkDay workDay = new WorkDay();
        workDay.setUserId(employee.getId());
        workDay.setEnterTime(startTime);
        workDay.setEnterSite(site);
        workDay.setEnterLocation(request.getLocation());

        workDayRepository.save(workDay);
        return new BasicResponse(true, null);
    }

    public BasicResponse exit(String token, ExitRequest request) {
        Employee employee = getEmployeeByToken(token);
        if (employee == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }

        WorkDay open = workDayRepository.findOpenByUserId(employee.getId());
        if (open == null) {
            return new BasicResponse(false, Errors.ERROR_EMPLOYEE_NOT_WORKING);
        }
        WorkingSite site;
        if (request.getSiteId() != null){
            site = persist.loadObject(WorkingSite.class, request.getSiteId());
        } else {
            site = null;
        }
        if(site != null && request.getLocation() != null){
            return new BasicResponse(false , Errors.ERROR_TWO_LOCATIONS_AT_ONCE);
        } else if (site == null && request.getLocation() == null) {
            return new BasicResponse(false , Errors.ERROR_NO_LOCATION_OR_SITE_PROVIDED);
        }
        Date endTime = request.getEndTime() != null? request.getEndTime() : new Date();
        open.setExitTime(endTime);
        open.setExitSite(site);
        open.setExitLocation(request.getLocation());
        workDayRepository.save(open);
        return new BasicResponse(true, null);
    }

    public WorkStatusResponse lastEnter(String token){
        Employee employee = getEmployeeByToken(token);
        if(employee == null){
            return new WorkStatusResponse(false, Errors.ERROR_INVALID_TOKEN, false, null);
        }
        WorkDay open = workDayRepository.findOpenByUserId(employee.getId());
        if (open == null) {
            return new WorkStatusResponse(true, Errors.ERROR_EMPLOYEE_NOT_WORKING, false, null);
        }
        return new WorkStatusResponse(true, null, true, open.getEnterTime());
    }

    public WorkListResponse getAllWorkList(String token) {
        Employee employee = getEmployeeByToken(token);
        if (employee == null) {
            return new WorkListResponse(false, Errors.ERROR_INVALID_TOKEN, null);
        }

        List<WorkDay> list = workDayRepository.findAllByUserId(employee.getId());
        return new WorkListResponse(true, null, list);
    }


    public SiteListResponse getAllSites(){
        try {
            List<WorkingSite> sites = persist.loadList(WorkingSite.class);
            return new SiteListResponse(true , null, sites);
        } catch (Exception e) {
            return new SiteListResponse(false, Errors.ERROR_FETCHING_SITES, null);
        }
    }

    public WorkHoursResponse getTotalHoursAtMonth (String token, Integer month){
        Employee employee = getEmployeeByToken(token);
        if(employee == null){
            return new WorkHoursResponse(false,Errors.ERROR_INVALID_TOKEN,null,month);
        }
        Double totalHours = workDayRepository.getTotalHoursByMonth(employee.getId(),month);
        return new WorkHoursResponse(true, null, totalHours, month);
    }
}
