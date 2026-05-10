package server_2026_b.server.controllers;

import org.springframework.web.bind.annotation.*;
import server_2026_b.server.requests.EnterRequest;
import server_2026_b.server.requests.ExitRequest;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.WorkListResponse;
import server_2026_b.server.responses.WorkStatusResponse;
import server_2026_b.server.service.WorkDayService;

@RestController
@RequestMapping("/work")
public class WorkDayController {
    private final WorkDayService workDayService;
    public WorkDayController(WorkDayService workDayService) {
        this.workDayService = workDayService;
    }
    @PostMapping("/enter")
    public BasicResponse enter(@CookieValue(value = "accessToken", required = false) String token,
                               @RequestBody EnterRequest request) {
        return workDayService.enter(token, request);
    }
    @PostMapping("/exit")
    public BasicResponse exit(@CookieValue(value = "accessToken", required = false) String token,
                              @RequestBody ExitRequest request) {
        return workDayService.exit(token, request);
    }
    @GetMapping("/status")
    public WorkStatusResponse lastEnter(@CookieValue(value = "accessToken", required = false) String token) {
        return workDayService.lastEnter(token);
    }
    @GetMapping("/list")
    public WorkListResponse getAllWorkList(@CookieValue(value = "accessToken", required = false) String token) {
        return workDayService.getAllWorkList(token);
    }

    @GetMapping("/sites")
    public BasicResponse getAllSites(){
        // TODO: return the list of Working sites
        return new BasicResponse();
    }
}
