package server_2026_b.server.controllers;

import org.springframework.web.bind.annotation.*;
import server_2026_b.server.requests.CreateEmployeeRequest;
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
    public EmployeeListResponse getAllActive(
            @CookieValue(value = "accessToken", required = false) String token) {
        return crudEmployeeService.getAllActive(token);
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
            @RequestParam Long employeeId) {
        return crudEmployeeService.deleteEmployee(token, employeeId);
    }
}
