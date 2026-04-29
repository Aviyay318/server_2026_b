package server_2026_b.server.utils;

import server_2026_b.server.entities.AuthToken;

import java.time.LocalDateTime;
import java.util.UUID;
import static server_2026_b.server.utils.Constants.TOKEN_VALID_HOURS;

public class TokenUtils {

    private TokenUtils(){}


    public static AuthToken createToken (Long userId, UserType userType){
        LocalDateTime now = LocalDateTime.now();
        String token = UUID.randomUUID().toString();
        return new AuthToken(token, userId, userType,
                now, now.plusHours(TOKEN_VALID_HOURS));
    }


    public static  boolean isExpired(AuthToken authToken) {
        if (authToken == null || authToken.getExpiresAt() == null) {
            return true;
        }
        return LocalDateTime.now().isAfter(authToken.getExpiresAt());
    }


    public static boolean isValid(AuthToken authToken) {
        if (authToken == null) {
            return false;
        }
        if (!isTokenTextValid(authToken.getToken())) {
            return false;
        }
        return !isExpired(authToken);
    }

    public static boolean isTokenTextValid(String token) {
        return token != null && !token.isBlank();
    }
}