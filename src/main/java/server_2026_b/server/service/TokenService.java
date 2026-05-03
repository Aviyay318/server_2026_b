package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server_2026_b.server.database.RefreshTokenRepository;
import server_2026_b.server.entities.RefreshToken;
import server_2026_b.server.entities.User;
import server_2026_b.server.security.JwtService;
import server_2026_b.server.utils.TokenUtils;
import server_2026_b.server.utils.UserType;

import java.time.LocalDateTime;

@Service
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public TokenService(RefreshTokenRepository refreshTokenRepository,
                        JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public String createRefreshToken(User user) {
        String refreshTokenValue = jwtService.generateRefreshToken(user);
        RefreshToken refreshToken = createRefreshTokenEntity(user, refreshTokenValue);
        refreshTokenRepository.deleteByUserId(user.getId());
        refreshTokenRepository.save(refreshToken);
       return refreshTokenValue;
    }
    public String createAccessToken(User user){
        String accessToken = jwtService.generateAccessToken(user);
        return accessToken;
    }




    public RefreshToken getValidRefreshToken(String refreshTokenValue) {
        if (!TokenUtils.isTokenTextValid(refreshTokenValue)) {
            return null;}
        if (!jwtService.isTokenValid(refreshTokenValue)) {
            deleteTokenIfExists(refreshTokenValue);
            return null;}
        if (!jwtService.isRefreshToken(refreshTokenValue)) {
            return null;}
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue);
        if (!TokenUtils.isValid(refreshToken)) {
            deleteIfInvalid(refreshToken);
            return null;}
        return refreshToken;
    }

    public boolean isValidAccessToken(String accessToken) {
        if (!TokenUtils.isTokenTextValid(accessToken)) {
            return false;}
        if (!jwtService.isTokenValid(accessToken)) {
            return false;}
        return jwtService.isAccessToken(accessToken);
    }

    public Long getUserIdFromAccessToken(String accessToken) {
        if (!isValidAccessToken(accessToken)) {
            return null;}
        return jwtService.extractUserId(accessToken);
    }

    public UserType getUserTypeFromAccessToken(String accessToken) {
        if (!isValidAccessToken(accessToken)) {
            return null;}
        String userType = jwtService.extractUserType(accessToken);
        return UserType.valueOf(userType);
    }

    public String refreshAccessToken(String refreshTokenValue) {
        RefreshToken refreshToken = getValidRefreshToken(refreshTokenValue);
        if (refreshToken == null) {
            return null;}
        User fakeUser = new User();
        fakeUser.setId(refreshToken.getUserId());
        fakeUser.setUserType(refreshToken.getUserType());
        fakeUser.setUsername(String.valueOf(refreshToken.getUserId()));
        return jwtService.generateAccessToken(fakeUser);
    }

    public boolean deleteIfInvalid(RefreshToken refreshToken) {
        if (TokenUtils.isValid(refreshToken)) {
            return false;}
        if (refreshToken != null) {
            refreshTokenRepository.delete(refreshToken);}
        return true;
    }

    public void deleteToken(RefreshToken refreshToken) {
        if (refreshToken != null) {
            refreshTokenRepository.delete(refreshToken);
        }
    }

    public void logout(String refreshTokenValue) {
        if (!TokenUtils.isTokenTextValid(refreshTokenValue)) {
            return;
        }

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue);
        if (refreshToken != null) {
            refreshTokenRepository.delete(refreshToken);
        }
    }

    private RefreshToken createRefreshTokenEntity(User user, String refreshTokenValue) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = TokenUtils.toLocalDateTime(
                jwtService.extractExpiration(refreshTokenValue)
        );
        return new RefreshToken(refreshTokenValue, now, expiresAt, user.getId(), user.getUserType()
        );
    }

    private void deleteTokenIfExists(String tokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenValue);
        if (refreshToken != null) {
            refreshTokenRepository.delete(refreshToken);
        }
    }
}