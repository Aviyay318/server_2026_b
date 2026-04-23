package server_2026_b.server.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/sse")
public class SseController {

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter(0L);

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                while (true) {
                    emitter.send(SseEmitter.event()
                            .name("heartbeat")
                            .data("server-time: " + LocalDateTime.now()));
                    Thread.sleep(2000);
                }
            } catch (IOException | InterruptedException ex) {
                emitter.completeWithError(ex);
            }
        });

        return emitter;
    }
}
