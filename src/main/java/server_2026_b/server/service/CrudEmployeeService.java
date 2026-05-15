package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.EmployeeRepository;
import server_2026_b.server.entities.ArchivedEmployee;
import server_2026_b.server.entities.Employee;
import server_2026_b.server.entities.Employer;
import server_2026_b.server.entities.relations.EmploymentRelation;
import server_2026_b.server.requests.CreateEmployeeRequest;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.EmployeeListResponse;
import server_2026_b.server.utils.Errors;
import server_2026_b.server.utils.GenerateHash;
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
        Employer employer = userService.getEmployerByAccessToken(token);
        if (employer == null)
            return new EmployeeListResponse(false, Errors.ERROR_INVALID_TOKEN);

        List<Employee> active = employeeRepository.findActiveEmployeesByEmployer(employer.getId());
        return new EmployeeListResponse(true, active);
    }

    public BasicResponse createEmployee(String token, CreateEmployeeRequest req) {
        Employer employer = userService.getEmployerByAccessToken(token);
        if (employer == null)
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);

        if (isBlank(req.getUsername()) || isBlank(req.getFirstName()) ||
            isBlank(req.getLastName())  || isBlank(req.getPassword()))
            return new BasicResponse(false, Errors.ERROR_EMPTY_FIELD);

        if (employeeRepository.existsByUsername(req.getUsername()))
            return new BasicResponse(false, Errors.ERROR_EMPLOYEE_ALREADY_EXISTS);

        Employee employee = new Employee(
                req.getUsername(),
                req.getFirstName(),
                req.getLastName(),
                GenerateHash.hashMd5(req.getUsername(), req.getPassword()),
                req.getPhone(),
                req.getEmail(),
                UserType.EMPLOYEE
        );

        employeeRepository.saveEmployee(employee);
        employeeRepository.saveRelation(new EmploymentRelation(employer, employee));

        return new BasicResponse(true, null);
    }

    public BasicResponse deleteEmployee(String token, Long employeeId) {
        Employer employer = userService.getEmployerByAccessToken(token);
        if (employer == null)
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);

        if (employer.getId().equals(employeeId))
            return new BasicResponse(false, Errors.ERROR_CANNOT_DELETE_EMPLOYER);

        Employee employee = employeeRepository.findEmployeeById(employeeId);
        if (employee == null)
            return new BasicResponse(false, Errors.ERROR_EMPLOYEE_NOT_FOUND);

        EmploymentRelation relation = employeeRepository.findRelation(employer.getId(), employeeId);
        if (relation == null)
            return new BasicResponse(false, Errors.ERROR_NOT_YOUR_EMPLOYEE);

        ArchivedEmployee archived = new ArchivedEmployee();
        archived.setUsername(employee.getUsername());
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
