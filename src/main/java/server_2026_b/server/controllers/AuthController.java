package server_2026_b.server.controllers;


import org.springframework.web.bind.annotation.*;
import server_2026_b.server.requests.LoginRequest;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.LoginResponse;
import server_2026_b.server.service.AuthService;
import server_2026_b.server.utils.CookieUtils;
import server_2026_b.server.utils.Errors;
import server_2026_b.server.utils.GenerateHash;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/loginEmployee")
    public LoginResponse loginEmployee(@RequestBody LoginRequest request,
                                       HttpServletResponse response) {
        LoginResponse loginResponse = authService.loginEmployee(
                request.getPersonalId(), request.getPassword());

        if (loginResponse.isSuccess()) {
            CookieUtils.setAuthCookies(response,
                    loginResponse.getAccessToken(),
                    loginResponse.getRefreshToken());
            stripTokensFromBody(loginResponse);
        }
        return loginResponse;
    }

    @PostMapping("/loginEmployer")
    public LoginResponse loginEmployer(@RequestBody LoginRequest request,
                                       HttpServletResponse response) {
        LoginResponse loginResponse = authService.loginEmployer(
                request.getPersonalId(), request.getPassword());

        if (loginResponse.isSuccess()) {
            CookieUtils.setAuthCookies(response,
                    loginResponse.getAccessToken(),
                    loginResponse.getRefreshToken());
            stripTokensFromBody(loginResponse);
        }
        return loginResponse;
    }

    @PostMapping("/login-admin")
    public LoginResponse loginAdmin(@RequestBody LoginRequest request,
                                       HttpServletResponse response) {
        LoginResponse loginResponse = authService.loginAdmin(
                request.getPersonalId(), request.getPassword());

        if (loginResponse.isSuccess()) {
            CookieUtils.setAuthCookies(response,
                    loginResponse.getAccessToken(),
                    loginResponse.getRefreshToken());
            stripTokensFromBody(loginResponse);
        }
        return loginResponse;
    }

    @PostMapping("/logout")
    public BasicResponse logoutUser(
            @CookieValue(value = "accessToken", required = false) String token,
            HttpServletResponse response) {
        BasicResponse basicResponse = authService.logout(token);
        CookieUtils.clearAuthCookies(response);
        return basicResponse;
    }

    private void stripTokensFromBody(LoginResponse loginResponse) {
        loginResponse.setAccessToken(null);
        loginResponse.setRefreshToken(null);
        loginResponse.setToken(null);
    }

    @PostMapping("/refresh")
    public BasicResponse refresh(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {
        String newAccessToken = authService.refresh(refreshToken);
        if (newAccessToken == null) {
            CookieUtils.clearAuthCookies(response);
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }
        CookieUtils.setAccessCookie(response, newAccessToken);
        return new BasicResponse(true, null);
    }
}
