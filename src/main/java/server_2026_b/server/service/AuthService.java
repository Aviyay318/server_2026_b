package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.entities.User;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.LoginResponse;
import server_2026_b.server.utils.GenerateHash;
import server_2026_b.server.utils.IdValidator;
import server_2026_b.server.utils.TokenUtils;
import server_2026_b.server.utils.UserType;

import static server_2026_b.server.utils.Errors.*;


@Service
public class AuthService {
    private final TokenService tokenService;
    private final UserService userService;
    private final TokenExpirySseService tokenExpirySseService;

    public AuthService( TokenService tokenService, UserService userService,
                       TokenExpirySseService tokenExpirySseService) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.tokenExpirySseService = tokenExpirySseService;
    }

    public LoginResponse loginEmployee(String personalId, String password) {
        return loginUser(personalId, password, UserType.EMPLOYEE);
    }

    public LoginResponse loginEmployer(String personalId, String password) {
        return loginUser(personalId, password, UserType.EMPLOYER);
    }

    public LoginResponse loginAdmin(String personalId, String password) {
        return loginUser(personalId, password, UserType.ADMIN);
    }


    private LoginResponse loginUser(String personalId, String password, UserType userType) {
        if (personalId == null || personalId.trim().isEmpty() || password == null || password.trim().isEmpty()
                || userType == null) {
            return new LoginResponse(ERROR_EMPTY_FIELD, false, "Missing personal id or password");
        }
        personalId = IdValidator.normalize(personalId);
        password = password.trim();

        if (!IdValidator.checkID(personalId)) {
            return new LoginResponse(ERROR_INVALID_ID, false, "Invalid Israeli ID");
        }

        String hashedPassword = GenerateHash.hashMd5(personalId, password);
        User user = userService.getUserByPersonalIdAndPasswordAndType(
               personalId, hashedPassword, userType);
        if (user == null) {
            return new LoginResponse(ERROR_WRONG_CREDENTIALS, false, "Wrong personal id or password");
        }

        String refreshToken = tokenService.createRefreshToken(user);
        String accessToken = tokenService.createAccessToken(user);
        return new LoginResponse(null, true, "Login successful",
                accessToken, refreshToken,
                user.getUserType()
        );
    }



    public BasicResponse logout(String token) {
        if (!TokenUtils.isTokenTextValid(token)) {
            return new BasicResponse(false, ERROR_INVALID_TOKEN);
        }
        Long userId = tokenService.getUserIdFromAccessToken(token);
        if (userId == null) {
            return new BasicResponse(false, ERROR_INVALID_TOKEN);
        }
        tokenService.logoutByUserId(userId);
        tokenExpirySseService.disconnect(userId);
        return new BasicResponse(true, null);
    }

    public String refresh(String refreshToken) {
        if (!TokenUtils.isTokenTextValid(refreshToken)) {
            return null;
        }
        return tokenService.refreshAccessToken(refreshToken);
    }

}
