package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.EmployeeRepository;
import server_2026_b.server.entities.ArchivedEmployee;
import server_2026_b.server.entities.User;
import server_2026_b.server.entities.relations.EmploymentRelation;
import server_2026_b.server.requests.CreateEmployeeRequest;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.EmployeeListResponse;
import server_2026_b.server.utils.Errors;
import server_2026_b.server.utils.GenerateHash;
import server_2026_b.server.utils.IdValidator;
import server_2026_b.server.utils.UserType;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CrudEmployeeService {

    private final UserService userService;
    private final EmployeeRepository employeeRepository;

    public CrudEmployeeService(UserService userService, EmployeeRepository employeeRepository) {
        this.userService = userService;
        this.employeeRepository = employeeRepository;
    }

    public EmployeeListResponse getAllActive(String token) {
        User employer = userService.getEmployerByAccessToken(token);
        if (employer == null)
            return new EmployeeListResponse(false, Errors.ERROR_INVALID_TOKEN);

        List<User> active = employeeRepository.findActiveEmployeesByEmployer(employer.getId());
        return new EmployeeListResponse(true, active);
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

    public BasicResponse deleteEmployee(String token, Long employeeId) {
        User employer = userService.getEmployerByAccessToken(token);
        if (employer == null)
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);

        if (employer.getId().equals(employeeId))
            return new BasicResponse(false, Errors.ERROR_CANNOT_DELETE_EMPLOYER);

        User employee = employeeRepository.findEmployeeById(employeeId);
        if (employee == null)
            return new BasicResponse(false, Errors.ERROR_EMPLOYEE_NOT_FOUND);

        if (!IdValidator.checkID(employee.getPersonalId()))
            return new BasicResponse(false, Errors.ERROR_INVALID_ID);

        EmploymentRelation relation = employeeRepository.findRelation(employer.getId(), employeeId);
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
        employeeRepository.deleteAllRelationsForEmployee(employeeId);
        employeeRepository.deleteEmployee(employee);

        return new BasicResponse(true, null);
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
