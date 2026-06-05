package server_2026_b.server.controllers;

import org.springframework.web.bind.annotation.*;
import server_2026_b.server.entities.EmployerSettings;
import server_2026_b.server.requests.SettingsRequest;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.SettingsResponse;
import server_2026_b.server.service.EmployerSettingsService;

@RestController
@RequestMapping("/employer-settings")
public class EmployerSettingsController {
    private final EmployerSettingsService settingsService;

    public EmployerSettingsController(EmployerSettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping("/get-settings")
    public SettingsResponse getSettings(@CookieValue(value = "accessToken" , required = false) String token) {
        return settingsService.getSettings(token);
    }

    @PostMapping("/set-settings")
    public BasicResponse setSettings(@CookieValue(value = "accessToken" , required = false)
                                        String token, @RequestBody SettingsRequest settingsRequest) {
        return settingsService.setSettings(token , settingsRequest);
    }
}
