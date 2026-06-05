package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.EmployerSettingsRepository;
import server_2026_b.server.entities.EmployerSettings;
import server_2026_b.server.entities.User;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.requests.SettingsRequest;
import server_2026_b.server.responses.SettingsResponse;
import server_2026_b.server.utils.Errors;

import java.time.LocalDateTime;

@Service
public class EmployerSettingsService {
    private final UserService userService;
    private final EmployerSettingsRepository employerSettingsRepository;

    public EmployerSettingsService(UserService userService, EmployerSettingsRepository employerSettingsRepository) {
        this.userService = userService;
        this.employerSettingsRepository = employerSettingsRepository;
    }

    public SettingsResponse getSettings(String token) {
        User employer = userService.getEmployerByAccessToken(token);
        if (employer == null) {
            return new SettingsResponse(false , Errors.ERROR_INVALID_TOKEN);
        }
        EmployerSettings employerSettings = employerSettingsRepository.findByEmployerId(employer.getId());
        if (employerSettings == null) {
            return new SettingsResponse(true ,(LocalDateTime) null);
        }
        return new SettingsResponse(true ,employerSettings.getSubmissionExpiration());
    }

    public BasicResponse setSettings(String token, SettingsRequest settingsRequest) {
        User employer = userService.getEmployerByAccessToken(token);
        if (employer == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }
        if (settingsRequest == null || settingsRequest.getSubmissionExpiration() == null) {
            return new BasicResponse(false, Errors.ERROR_EMPTY_FIELD);
        }
        EmployerSettings employerSettings = employerSettingsRepository.findByEmployerId(employer.getId());
        if (employerSettings != null) {
            //עדכון של רשומה קיימת
            employerSettings.setSubmissionExpiration(settingsRequest.getSubmissionExpiration());
        }else {
            employerSettings = new EmployerSettings(employer.getId(), settingsRequest.getSubmissionExpiration());
        }
        employerSettingsRepository.save(employerSettings);
        return new BasicResponse(true, null);
    }


}
