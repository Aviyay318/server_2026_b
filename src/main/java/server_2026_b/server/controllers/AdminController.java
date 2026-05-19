package server_2026_b.server.controllers;

import org.springframework.web.bind.annotation.*;
import server_2026_b.server.requests.CreateEmployerRequest;
import server_2026_b.server.responses.AdminResponse;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/general-info")
    public BasicResponse generalInfo(@CookieValue(value = "accessToken", required = false) String token) {
        return adminService.getAllUsers(token);
    }

    @GetMapping("/employers-list")
    public BasicResponse employersList(@CookieValue(value = "accessToken", required = false) String token) {
        return adminService.getAllEmployers(token);
    }

    @GetMapping("/employer-worker")
    public BasicResponse employerWorker(@CookieValue(value = "accessToken", required = false) String token ,
            @RequestParam Long employerId) {
        return adminService.getEmployeesByEmployerId(token, employerId);
    }

    @GetMapping("/realtime-info")
    public BasicResponse realtimeInfo(@CookieValue(value = "accessToken", required = false) String token) {
        // כאן אנחנו מחזירים list של משתמשים פעילים (מי שהטוקן שלו בתוקף והוא לא אדמין)
        return adminService.getActiveUsers(token);
    }

    @PostMapping("/create-employer")
    public BasicResponse createEmployer(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestBody CreateEmployerRequest request) { // מעסיק עם ת״ז כסטרינג
        return adminService.createEmployer(token, request);
    }

    @DeleteMapping("/delete-employer")
    public BasicResponse deleteEmployer(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestParam String employerId) { // יקבל ת״ז כסטרינג
        return adminService.deleteEmployer(token, employerId);
    }
}
