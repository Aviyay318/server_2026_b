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
    // TODO: endpoints:
    //	- enter { זמן, תאריך, מיקום, טוקן } => BasicResponse
    //	- exit { זמן, תאריך, מיקום, תוקן } => BasicResponse
    //	- lastEnter { טוקן } => זמן כניסה אחרון ללא יציאה
    //                          BasicResponse + Date
    //	- getAllWorkList { טוקן } => List<WorkDay>

    private final WorkDayService workDayService;


    public WorkDayController(WorkDayService workDayService) {
        this.workDayService = workDayService;
    }

    @PostMapping("/enter")
    public BasicResponse enter(@RequestBody EnterRequest request){
        return workDayService.enter(request);
    }

    @PostMapping("/exit")
    public BasicResponse exit(@RequestBody ExitRequest request) {
        return workDayService.exit(request);
    }

    @GetMapping("/status")
    public WorkStatusResponse lastEnter(@RequestHeader("Authorization") String token) {
        return workDayService.lastEnter(token);
    }

    @GetMapping("/list")
    public WorkListResponse getAllWorkList(@RequestHeader("Authorization") String token) {
        return workDayService.getAllWorkList(token);
    }
}
