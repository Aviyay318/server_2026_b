package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import server_2026_b.server.database.RefreshTokenRepository;
import server_2026_b.server.entities.RefreshToken;
import server_2026_b.server.security.JwtService;
import server_2026_b.server.utils.TokenUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class TokenExpirySseService {

    private static final long EMITTER_BUFFER_MS = 5000;

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ScheduledExecutorService scheduler;

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public TokenExpirySseService(JwtService jwtService,
                                 RefreshTokenRepository refreshTokenRepository,
                                 ScheduledExecutorService scheduler) {
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.scheduler = scheduler;
    }

    public SseEmitter register(String token) {
        if (!TokenUtils.isTokenTextValid(token)
                || !jwtService.isTokenValid(token)
                || !jwtService.isAccessToken(token)) {
            throw new IllegalArgumentException("Invalid access token");
        }

        Long userId = jwtService.extractUserId(token);
        Date expiration = jwtService.extractExpiration(token);
        long delayMs = expiration.getTime() - System.currentTimeMillis();


        cleanup(userId);

        SseEmitter emitter = new SseEmitter(Math.max(delayMs, 0) + EMITTER_BUFFER_MS);
        emitter.onCompletion(() -> cleanup(userId));
        emitter.onTimeout(() -> cleanup(userId));
        emitter.onError(throwable -> cleanup(userId));

        emitters.put(userId, emitter);

        if (delayMs <= 0) {
            notifyExpired(userId);
            return emitter;
        }

        ScheduledFuture<?> future = scheduler.schedule(
                () -> notifyExpired(userId),
                delayMs,
                TimeUnit.MILLISECONDS
        );
        scheduledTasks.put(userId, future);


        return emitter;
    }

    public void disconnect(Long userId) {
        cleanup(userId);
    }

    private void notifyExpired(Long userId) {

        SseEmitter emitter = emitters.get(userId);
        if (emitter == null) {
            return;
        }

        try {
            emitter.send(SseEmitter.event()
                    .name("TOKEN_EXPIRED")
                    .data("Your access token has expired"));
        } catch (IOException ignored) {
        }

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId);
        if (refreshToken != null && TokenUtils.isExpired(refreshToken)) {
            refreshTokenRepository.deleteByUserId(userId);
        }

        try {
            emitter.complete();
        } catch (Exception ignored) {
        }
    }

    private void cleanup(Long userId) {
        SseEmitter emitter = emitters.remove(userId);
        if (emitter != null) {
            try {
                emitter.complete();
            } catch (Exception ignored) {
            }
        }

        ScheduledFuture<?> future = scheduledTasks.remove(userId);
        if (future != null) {
            future.cancel(false);
        }
    }
}
