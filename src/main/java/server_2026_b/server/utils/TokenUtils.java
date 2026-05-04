package server_2026_b.server.utils;

import server_2026_b.server.entities.RefreshToken;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TokenUtils {

    private TokenUtils() {
    }

    public static boolean isTokenTextValid(String token) {
        return token != null && !token.isBlank();
    }

    public static boolean isExpired(RefreshToken refreshToken) {
        if (refreshToken == null || refreshToken.getExpiresAt() == null) {
            return true;
        }

        return LocalDateTime.now().isAfter(refreshToken.getExpiresAt());
    }

    public static boolean isValid(RefreshToken refreshToken) {
        if (refreshToken == null) {
            return false;
        }

        if (!isTokenTextValid(refreshToken.getToken())) {
            return false;
        }

        return !isExpired(refreshToken);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(date.getTime()),
                ZoneId.systemDefault()
        );
    }
}