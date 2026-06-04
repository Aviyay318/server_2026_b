package server_2026_b.server.controllers;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import server_2026_b.server.requests.ShiftListRequest;
import server_2026_b.server.requests.ShiftRequest;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.ShiftListResponse;
import server_2026_b.server.service.ShiftService;

@RestController
@RequestMapping("/employer-settings")
public class EmployerSettingsController {

    private final ShiftService shiftService;

    public EmployerSettingsController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @PostMapping("/set-shifts")
    public BasicResponse setShifts(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestBody ShiftListRequest request) {

        return shiftService.setShifts(token, request);
    }

    @GetMapping("/get-shifts")
    public ShiftListResponse getShifts(
            @CookieValue(value = "accessToken", required = false) String token) {

        return shiftService.getShifts(token);
    }

    @PostMapping("/post-shifts")
    public ShiftListResponse postShifts(
            @CookieValue(value = "accessToken", required = false) String token) {

        return shiftService.postShifts(token);
    }

    @PostMapping("/update-shift")
    public BasicResponse updateShift(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestBody ShiftRequest request) {

        return shiftService.updateShift(token, request);
    }

    @DeleteMapping("/delete-shift")
    public BasicResponse deleteShift(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestParam Long id) {

        return shiftService.deleteShift(token, id);
    }
}