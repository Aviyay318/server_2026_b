package server_2026_b.server.service;
import org.springframework.stereotype.Service;
import server_2026_b.server.database.AuthTokenRepository;
import server_2026_b.server.entities.AuthToken;
import server_2026_b.server.utils.TokenUtils;
import server_2026_b.server.utils.UserType;


@Service
public class TokenService {
    private final AuthTokenRepository authTokenRepository;

    public TokenService(AuthTokenRepository authTokenRepository) {
        this.authTokenRepository = authTokenRepository;
    }

    public AuthToken createAndSaveToken(Long userId, UserType userType) {
        AuthToken authToken = TokenUtils.createToken(userId, userType);
        authTokenRepository.save(authToken);
        return authToken;
    }

    public AuthToken getValidToken(String token) {
        if (!TokenUtils.isTokenTextValid(token)) {
            return null;}
        AuthToken authToken = authTokenRepository.findByToken(token);
        if (!TokenUtils.isValid(authToken)) {
            deleteIfInvalid(authToken);
            return null;}
        return authToken;
    }

    public boolean deleteIfInvalid(AuthToken authToken) {
        if (TokenUtils.isValid(authToken)) {
            return false;}
        if (authToken != null) {
            authTokenRepository.delete(authToken);}
        return true;}

    public void deleteToken(AuthToken authToken) {
        if (authToken != null) {
            authTokenRepository.delete(authToken);}
    }
}