package server_2026_b.server.controllers;

import org.springframework.web.bind.annotation.*;
import server_2026_b.server.responses.ShiftResponse;
import server_2026_b.server.service.ShiftService;

@RestController
@RequestMapping("/shifts")
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping("/get-all")
    public ShiftResponse getAllShifts(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestParam String fromDate,
            @RequestParam String toDate) {

        return shiftService.getWeeklyShifts(token, fromDate, toDate);
    }
}