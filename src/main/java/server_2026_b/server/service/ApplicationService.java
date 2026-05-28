package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.ApplicationRepository;
import server_2026_b.server.database.EmployeeRepository;
import server_2026_b.server.database.WorkDayRepository;
import server_2026_b.server.entities.Application;
import server_2026_b.server.entities.User;
import server_2026_b.server.entities.WorkDay;
import server_2026_b.server.requests.AnswerApplicationRequest;
import server_2026_b.server.requests.ReportApplicationRequest;
import server_2026_b.server.responses.ApplicationListResponse;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.utils.ApplicationStatus;
import server_2026_b.server.utils.Errors;
import server_2026_b.server.utils.ShiftStatus;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final EmployeeRepository employeeRepository;
    private final WorkDayRepository workDayRepository;
    private final UserService userService;

    public ApplicationService(ApplicationRepository applicationRepository,
                              EmployeeRepository employeeRepository,
                              WorkDayRepository workDayRepository,
                              UserService userService) {
        this.applicationRepository = applicationRepository;
        this.employeeRepository = employeeRepository;
        this.workDayRepository = workDayRepository;
        this.userService = userService;
    }

    public BasicResponse report(String token, ReportApplicationRequest request) {
        User employee = userService.getEmployeeByAccessToken(token);
        if (employee == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }

        if (request == null || request.getReason() == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_ABSENCE_REASON);
        }

        User employer = employeeRepository.findEmployerForEmployee(employee.getId());
        if (employer == null) {
            return new BasicResponse(false, Errors.ERROR_NO_EMPLOYER_FOUND);
        }

        Timestamp date = request.getDate() != null
                ? request.getDate()
                : new Timestamp(System.currentTimeMillis());

        Application application = new Application(
                employee.getId(),
                employer.getId(),
                date,
                request.getReason(),
                ApplicationStatus.WAITING
        );

        applicationRepository.save(application);

        return new BasicResponse(true, null);
    }

    public ApplicationListResponse getAll(String token) {
        User employer = userService.getEmployerByAccessToken(token);
        if (employer == null) {
            return new ApplicationListResponse(false, Errors.ERROR_INVALID_TOKEN, null);
        }

        List<Application> waiting = applicationRepository.findWaitingByEmployer(employer.getId());
        return new ApplicationListResponse(true, null, waiting);
    }

    public BasicResponse answer(String token, AnswerApplicationRequest request) {
        User employer = userService.getEmployerByAccessToken(token);
        if (employer == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }

        if (request == null || request.getApplicationId() == null) {
            return new BasicResponse(false, Errors.ERROR_EMPTY_FIELD);
        }

        Application application = applicationRepository.findById(request.getApplicationId());
        if (application == null) {
            return new BasicResponse(false, Errors.ERROR_APPLICATION_NOT_FOUND);
        }

        if (!employer.getId().equals(application.getEmployerId())) {
            return new BasicResponse(false, Errors.ERROR_APPLICATION_NOT_YOURS);
        }

        if (application.getStatus() != ApplicationStatus.WAITING) {
            return new BasicResponse(false, Errors.ERROR_APPLICATION_ALREADY_ANSWERED);
        }

        if (request.isAccept()) {
            application.setStatus(ApplicationStatus.ACCEPTED);
            applicationRepository.save(application);

            WorkDay workDay = new WorkDay();
            workDay.setUserId(application.getEmployeeId());
            workDay.setEnterTime(application.getDate());
            workDay.setStatus(ShiftStatus.ABSENCE);
            workDay.setAbsenceReason(application.getReason());
            workDayRepository.save(workDay);
        } else {
            application.setStatus(ApplicationStatus.DENIED);
            applicationRepository.save(application);
        }

        return new BasicResponse(true, null);
    }
}
