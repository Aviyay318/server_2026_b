package server_2026_b.server.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import server_2026_b.server.service.TokenExpirySseService;

@RestController
@RequestMapping("/auth")
public class SseController {

    private final TokenExpirySseService tokenExpirySseService;

    public SseController(TokenExpirySseService tokenExpirySseService) {
        this.tokenExpirySseService = tokenExpirySseService;
    }

    @GetMapping(value = "/token-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@CookieValue(value = "accessToken", required = false) String token) {
        return tokenExpirySseService.register(token);
    }
}