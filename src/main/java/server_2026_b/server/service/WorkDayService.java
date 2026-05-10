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
import server_2026_b.server.responses.WorkListResponse;
import server_2026_b.server.responses.WorkStatusResponse;
import server_2026_b.server.utils.Errors;
import server_2026_b.server.utils.UserType;

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
//        Long userId = tokenService.getUserIdFromAccessToken(token);
//        UserType userType = tokenService.getUserTypeFromAccessToken(token);
//
//        if (userId == null || userType == null) {
//            return null;
//        }
//        if (userType != UserType.EMPLOYEE) {
//            return null;
//        }
        return userService.getEmployeeByAccessToken(token);
    }

    public BasicResponse enter(EnterRequest request){
        Employee employee = getEmployeeByToken(request.getToken());
        if(employee == null){
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }
        WorkDay open = workDayRepository.findOpenByUserId(employee.getId());
        if(open != null){
            return new BasicResponse(false, Errors.ERROR_EMPLOYEE_ALREADY_WORKING);
        }

        WorkingSite site = persist.loadObject(WorkingSite.class, request.getSiteId());
        if(site == null){
            return new BasicResponse(false , Errors.ERROR_SITE_NOT_FOUND);
        }

        WorkDay workDay = new WorkDay(employee.getId(), request.getStartTime(), site);
        workDayRepository.save(workDay);
        return new BasicResponse(true, null);
    }

    public BasicResponse exit(ExitRequest request) {
        Employee employee = getEmployeeByToken(request.getToken());
        if (employee == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }

        WorkDay open = workDayRepository.findOpenByUserId(employee.getId());
        if (open == null) {
            return new BasicResponse(false, Errors.ERROR_EMPLOYEE_NOT_WORKING);
        }

        open.setExitTime(request.getEndTime());
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
}
