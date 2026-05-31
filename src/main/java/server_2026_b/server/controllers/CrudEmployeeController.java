package server_2026_b.server.controllers;

import org.springframework.web.bind.annotation.*;
import server_2026_b.server.requests.CreateEmployeeRequest;
import server_2026_b.server.requests.EmployeeDateRequest;
import server_2026_b.server.responses.ActiveEmployeeListResponse;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.EmployeeListResponse;
import server_2026_b.server.responses.EmployeeResponse;
import server_2026_b.server.service.CrudEmployeeService;

@RestController
@RequestMapping("/crud-employee")
public class CrudEmployeeController {

    private final CrudEmployeeService crudEmployeeService;

    public CrudEmployeeController(CrudEmployeeService crudEmployeeService) {
        this.crudEmployeeService = crudEmployeeService;
    }

    @GetMapping("/get-all-active")
    public ActiveEmployeeListResponse getAllActive(
            @CookieValue(value = "accessToken", required = false) String token) {
        return crudEmployeeService.getAllActive(token);
    }

    @PostMapping("/exited-employees")
    public EmployeeListResponse getExitedEmployees(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestBody EmployeeDateRequest request) {
        return crudEmployeeService.getExitedEmployees(token, request);
    }

    @PostMapping("/absenced-employees")
    public EmployeeListResponse getAbsencedEmployees(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestBody EmployeeDateRequest request) {
        return crudEmployeeService.getAbsencedEmployees(token, request);
    }

    @GetMapping("/employee-by-id")
    public EmployeeResponse getEmployeeById(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestParam Long employeeId) {
        return crudEmployeeService.getEmployeeById(token, employeeId);
    }

    @GetMapping("/all-employees")
    public EmployeeListResponse getAllEmployee(
            @CookieValue(value = "accessToken", required = false) String token) {
        return crudEmployeeService.getAllEmployee(token);
    }

    @PostMapping("/create-employee")
    public BasicResponse createEmployee(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestBody CreateEmployeeRequest request) {
        return crudEmployeeService.createEmployee(token, request);
    }

    @DeleteMapping("/delete-employee")
    public BasicResponse deleteEmployee(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestParam String personalId) {
        return crudEmployeeService.deleteEmployee(token, personalId);
    }
}
