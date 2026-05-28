package server_2026_b.server.controllers;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server_2026_b.server.requests.AnswerApplicationRequest;
import server_2026_b.server.requests.ReportApplicationRequest;
import server_2026_b.server.responses.ApplicationListResponse;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.service.ApplicationService;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/report")
    public BasicResponse report(@CookieValue(value = "accessToken", required = false) String token,
                                @RequestBody ReportApplicationRequest request) {
        return applicationService.report(token, request);
    }

    @GetMapping("/get-all")
    public ApplicationListResponse getAll(@CookieValue(value = "accessToken", required = false) String token) {
        return applicationService.getAll(token);
    }

    @PostMapping("/answer")
    public BasicResponse answer(@CookieValue(value = "accessToken", required = false) String token,
                                @RequestBody AnswerApplicationRequest request) {
        return applicationService.answer(token, request);
    }
}
