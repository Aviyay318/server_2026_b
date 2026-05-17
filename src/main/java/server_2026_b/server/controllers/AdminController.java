package server_2026_b.server.controllers;

import org.springframework.web.bind.annotation.*;
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
    public BasicResponse generalInfo() {
        return new AdminResponse(true , null ,adminService.getAllUsers());
    }

    @GetMapping("/employers-list")
    public BasicResponse employersList() {
        return new AdminResponse(true , null , adminService.getAllEmployers());
    }

    @GetMapping("/employer-worker")
    public BasicResponse employerWorker(@RequestParam Long employerId) {
        return new AdminResponse(true , null , adminService.getEmployeesByEmployerId(employerId));
    }

    @GetMapping("/realtime-info")
    public BasicResponse realtimeInfo() {
        // כאן אנחנו מחזירים list של משתמשים פעילים (מי שהטוקן שלו בתוקף והוא לא אדמין)
        return new AdminResponse(true , null , adminService.getActiveUsers());
    }
}
