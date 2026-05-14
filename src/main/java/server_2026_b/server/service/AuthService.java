package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.entities.RefreshToken;
import server_2026_b.server.entities.Employee;
import server_2026_b.server.entities.Employer;
import server_2026_b.server.entities.User;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.LoginResponse;
import server_2026_b.server.utils.Errors;
import server_2026_b.server.utils.GenerateHash;
import server_2026_b.server.utils.TokenUtils;
import server_2026_b.server.utils.UserType;

import static server_2026_b.server.utils.Errors.*;


@Service
public class AuthService { // מחוברת לדאטה בייס ע״י Persist
    private final Persist persist;
    private final TokenService tokenService;
    private final UserService userService;
    private final TokenExpirySseService tokenExpirySseService;

    public AuthService(Persist persist, TokenService tokenService, UserService userService,
                       TokenExpirySseService tokenExpirySseService) {
        this.persist = persist;
        this.tokenService = tokenService;
        this.userService = userService;
        this.tokenExpirySseService = tokenExpirySseService;
    }

    public LoginResponse loginEmployee(String username, String password) {
        return loginUser(username, password, UserType.EMPLOYEE);
    }

    public LoginResponse loginEmployer(String username, String password) {
        return loginUser(username, password, UserType.EMPLOYER);
    }


    private LoginResponse loginUser(String username, String password, UserType userType) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()
                || userType == null) {return new LoginResponse(ERROR_EMPTY_FIELD, false, "Missing username or password");
        }
        username = username.trim();
        password = password.trim();
        String hashedPassword = GenerateHash.hashMd5(username, password);
        User user = userService.getUserByUsernameAndPasswordAndType(
               username, hashedPassword, userType);
        if (user == null) {
            return new LoginResponse(ERROR_WRONG_CREDENTIALS, false, "Wrong username or password");}

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