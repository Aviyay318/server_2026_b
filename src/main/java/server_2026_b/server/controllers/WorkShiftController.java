package server_2026_b.server.controllers;

import org.springframework.web.bind.annotation.*;
import server_2026_b.server.entities.WorkShift;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.WorkShiftResponse;
import server_2026_b.server.service.WorkShiftService;

import java.util.List;

@RestController
@RequestMapping("/work-shifts")
public class WorkShiftController {

    private final WorkShiftService workShiftService;

    public WorkShiftController(WorkShiftService workShiftService) {
        this.workShiftService = workShiftService;
    }

    @PostMapping("/save-placement")
    public BasicResponse savePlacement(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestBody List<WorkShift> placements) {
        return workShiftService.savePlacements(token, placements);
    }

    @PutMapping("/update-placement")
    public BasicResponse updatePlacement(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestBody WorkShift placement) {
        return workShiftService.updatePlacement(token, placement);
    }

    @GetMapping("/get-all")
    public WorkShiftResponse getAllPlacements(
            @CookieValue(value = "accessToken", required = false) String token) {
        return workShiftService.getAllPlacements(token);
    }

    @GetMapping("/get-for-employee")
    public WorkShiftResponse getPlacementsForEmployee(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestParam Long employeeId) {
        return workShiftService.getPlacementsForEmployee(token, employeeId);
    }
}