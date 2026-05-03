package server_2026_b.server.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import server_2026_b.server.entities.User;
import server_2026_b.server.utils.Constants;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final JwtConfig jwtConfig;
    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtConfig.getAccessExpiration());
        return Jwts.builder()
                .subject(user.getUsername())
                .claim(Constants.USER_ID, user.getId())
                .claim(Constants.USER_TYPE, user.getUserType().toString())
                .claim(Constants.TOKEN_TYPE, Constants.ACCESS_TOKEN)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtConfig.getRefreshExpiration());
        return Jwts.builder()
                .subject(user.getUsername())
                .claim(Constants.USER_ID, user.getId())
                .claim(Constants.USER_TYPE, user.getUserType().toString())
                .claim(Constants.TOKEN_TYPE, Constants.REFRESH_TOKEN)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get(Constants.USER_ID, Long.class);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractUserType(String token) {
        return extractAllClaims(token).get(Constants.USER_TYPE, String.class);
    }

    public String extractTokenType(String token) {
        return extractAllClaims(token).get(Constants.TOKEN_TYPE, String.class);
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    public boolean isAccessToken(String token) {
        return Constants.ACCESS_TOKEN.equals(extractTokenType(token));
    }

    public boolean isRefreshToken(String token) {
        return Constants.REFRESH_TOKEN.equals(extractTokenType(token));
    }
}