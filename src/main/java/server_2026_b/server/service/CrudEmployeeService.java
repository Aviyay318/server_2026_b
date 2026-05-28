package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.EmployeeRepository;
import server_2026_b.server.database.WorkDayRepository;
import server_2026_b.server.entities.ArchivedEmployee;
import server_2026_b.server.entities.User;
import server_2026_b.server.entities.WorkDay;
import server_2026_b.server.entities.relations.EmploymentRelation;
import server_2026_b.server.requests.CreateEmployeeRequest;
import server_2026_b.server.requests.EmployeeDateRequest;
import server_2026_b.server.responses.ActiveEmployeeDTO;
import server_2026_b.server.responses.ActiveEmployeeListResponse;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.EmployeeListResponse;
import server_2026_b.server.responses.EmployeeResponse;
import server_2026_b.server.responses.UserDTO;
import server_2026_b.server.utils.Errors;
import server_2026_b.server.utils.GenerateHash;
import server_2026_b.server.utils.IdValidator;
import server_2026_b.server.utils.UserType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrudEmployeeService {

    private final UserService userService;
    private final EmployeeRepository employeeRepository;
    private final WorkDayRepository workDayRepository;

    public CrudEmployeeService(UserService userService, EmployeeRepository employeeRepository, WorkDayRepository workDayRepository) {
        this.userService = userService;
        this.employeeRepository = employeeRepository;
        this.workDayRepository = workDayRepository;
    }

    // הופך רשימת User לרשימת UserDTO
    private List<UserDTO> convertToDtoList(List<User> users) {
        return users.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public ActiveEmployeeListResponse getAllActive(String token) {
        User employer = userService.getEmployerByAccessToken(token);
        if (employer == null)
            return new ActiveEmployeeListResponse(false, Errors.ERROR_INVALID_TOKEN);

        List<User> active = employeeRepository.findActiveEmployeesByEmployer(employer.getId());
        List<ActiveEmployeeDTO> result = active.stream()
                .map(employee -> {
                    WorkDay workDay = workDayRepository.findOpenByUserId(employee.getId());
                    String location = null;
                    if (workDay != null) {
                        if (workDay.getEnterLocation() != null) {
                            location = workDay.getEnterLocation();
                        } else if (workDay.getEnterSite() != null) {
                            location = workDay.getEnterSite().getName();
                        }
                    }
                    return new ActiveEmployeeDTO(
                            new UserDTO(employee),
                            workDay != null ? workDay.getEnterTime() : null,
                            location
                    );
                })
                .collect(Collectors.toList());
        return new ActiveEmployeeListResponse(true, result);
    }

    public EmployeeListResponse getExitedEmployees(String token, EmployeeDateRequest request) {
        User employer = userService.getEmployerByAccessToken(token);
        if (employer == null)
            return new EmployeeListResponse(false, Errors.ERROR_INVALID_TOKEN);

        if (request == null || request.getDate() == null)
            return new EmployeeListResponse(false, Errors.ERROR_EMPTY_FIELD);

        List<User> exited = employeeRepository.findExitedEmployeesByEmployerAndDate(employer.getId(), request.getDate());
        return new EmployeeListResponse(true, convertToDtoList(exited));
    }

    public EmployeeListResponse getAbsencedEmployees(String token, EmployeeDateRequest request) {
        User employer = userService.getEmployerByAccessToken(token);
        if (employer == null)
            return new EmployeeListResponse(false, Errors.ERROR_INVALID_TOKEN);

        if (request == null || request.getDate() == null)
            return new EmployeeListResponse(false, Errors.ERROR_EMPTY_FIELD);

        List<User> absenced = employeeRepository.findAbsencedEmployeesByEmployerAndDate(employer.getId(), request.getDate());
        return new EmployeeListResponse(true, convertToDtoList(absenced));
    }

    public BasicResponse createEmployee(String token, CreateEmployeeRequest req) {
        User employer = userService.getEmployerByAccessToken(token);
        if (employer == null)
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);

        if (isBlank(req.getPersonalId()) || isBlank(req.getFirstName()) ||
            isBlank(req.getLastName())  || isBlank(req.getPassword()))
            return new BasicResponse(false, Errors.ERROR_EMPTY_FIELD);

        if (!IdValidator.checkID(req.getPersonalId()))
            return new BasicResponse(false, Errors.ERROR_INVALID_ID);

        String normalizedPersonalId = IdValidator.normalize(req.getPersonalId());

        if (employeeRepository.existsByPersonalId(normalizedPersonalId))
            return new BasicResponse(false, Errors.ERROR_EMPLOYEE_ALREADY_EXISTS);

        User employee = new User(
                normalizedPersonalId,
                req.getFirstName(),
                req.getLastName(),
                GenerateHash.hashMd5(normalizedPersonalId, req.getPassword()),
                req.getPhone(),
                req.getEmail(),
                UserType.EMPLOYEE
        );

        employeeRepository.saveEmployee(employee);
        employeeRepository.saveRelation(new EmploymentRelation(employer, employee));

        return new BasicResponse(true, null);
    }

    public BasicResponse deleteEmployee(String token, String personalId) {
        User employer = userService.getEmployerByAccessToken(token);
        if (employer == null)
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);

        if (isBlank(personalId))
            return new BasicResponse(false, Errors.ERROR_EMPTY_FIELD);

        if (!IdValidator.checkID(personalId))
            return new BasicResponse(false, Errors.ERROR_INVALID_ID);

        String normalizedPersonalId = IdValidator.normalize(personalId);

        if (normalizedPersonalId.equals(employer.getPersonalId()))
            return new BasicResponse(false, Errors.ERROR_CANNOT_DELETE_EMPLOYER);

        User employee = employeeRepository.findEmployeeByPersonalId(normalizedPersonalId);
        if (employee == null)
            return new BasicResponse(false, Errors.ERROR_EMPLOYEE_NOT_FOUND);

        EmploymentRelation relation = employeeRepository.findRelation(employer.getId(), employee.getId());
        if (relation == null)
            return new BasicResponse(false, Errors.ERROR_NOT_YOUR_EMPLOYEE);

        ArchivedEmployee archived = new ArchivedEmployee();
        archived.setPersonalId(employee.getPersonalId());
        archived.setFirstName(employee.getFirstName());
        archived.setLastName(employee.getLastName());
        archived.setPassword(employee.getPassword());
        archived.setPhone(employee.getPhone());
        archived.setEmail(employee.getEmail());
        archived.setUserType(employee.getUserType());
        archived.setDeletedAt(LocalDateTime.now());
        archived.setDeletedByEmployerId(employer.getId());

        employeeRepository.archiveEmployee(archived);
        employeeRepository.deleteAllRelationsForEmployee(employee.getId());
        employeeRepository.deleteEmployee(employee);

        return new BasicResponse(true, null);
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public EmployeeResponse getEmployeeById(String token, Long employeeId) {
        User employer = userService.getUserByAccessTokenAndType(token, UserType.EMPLOYER);
        if (employer == null)
            return new EmployeeResponse(false, Errors.ERROR_INVALID_TOKEN);

        User emp = employeeRepository.findEmployeeById(employeeId);
        if (emp == null)
            return new EmployeeResponse(false, Errors.ERROR_EMPLOYEE_NOT_FOUND);
        if (employeeRepository.findRelation(employer.getId(), employeeId) == null ||
                !emp.getUserType().equals(UserType.EMPLOYEE)){
            return new EmployeeResponse(false, Errors.ERROR_NOT_YOUR_EMPLOYEE);
        }

        List<WorkDay> allWorkDays = workDayRepository.findAllByUserId(employeeId);
        System.out.println("allWorkDays: " + allWorkDays.toString());

        return new EmployeeResponse(true, null, new UserDTO(emp), allWorkDays);
    }

    public EmployeeListResponse getAllEmployee(String token) {
        User employer = userService.getUserByAccessTokenAndType(token, UserType.EMPLOYER);
        if (employer == null)
            return new EmployeeListResponse(false, Errors.ERROR_INVALID_TOKEN);

        List<User> employees =
                employeeRepository.findAllEmployeesByEmployer(employer.getId());

        return new EmployeeListResponse(true, convertToDtoList(employees));
    }
}
