package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.entities.RefreshToken;
import server_2026_b.server.entities.Employee;
import server_2026_b.server.entities.Employer;
import server_2026_b.server.responses.LoginResponse;
import server_2026_b.server.utils.Errors;
import server_2026_b.server.utils.GenerateHash;
import server_2026_b.server.utils.UserType;

import static server_2026_b.server.utils.Errors.*;


@Service
public class AuthService { // מחוברת לדאטה בייס ע״י Persist
    private final Persist persist;
    private final TokenService tokenService;
    private final UserService userService;

    public AuthService(Persist persist, TokenService tokenService, UserService userService) {
        this.persist = persist;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    public LoginResponse loginEmployee(String username, String password) { // הצעה לעשות את המתודה גנרית כי התחברות עובד ומעסיק דומות מאוד או לעשות loginUser אחד
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return new LoginResponse(ERROR_EMPTY_FIELD, false, "Missing username or password", null, null);
        }
        username = username.trim();
        password = password.trim();
        String hashedPassword = GenerateHash.hashMd5(username, password);
//        Employee employee = persist.getEmployeeByUsernameAndPassword(username, hashedPassword);
        Employee employee = userService.getEmployeeByUsernameAndPassword(username, hashedPassword);
        if (employee == null) {
            return new LoginResponse(ERROR_WRONG_CREDENTIALS, false, "Wrong username or password", null, null);
        }
//        RefreshToken refreshToken = tokenService.createAndSaveToken(employee.getId(), UserType.EMPLOYEE);
        String refreshToken = tokenService.createRefreshToken(employee);
        String accessToken = tokenService.createAccessToken(employee);

        //return new LoginResponse(null, true, "Login successful", refreshToken.getToken(), UserType.EMPLOYEE);
        return new LoginResponse(null, true, "Login successful", accessToken, refreshToken, employee.getUserType());
    }

    public LoginResponse loginEmployer(String username, String password) {
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return new LoginResponse(Errors.ERROR_EMPTY_FIELD, false,
                    "Missing username or password", null, null);
        }
        username = username.trim();
        password = password.trim();
        String hashedPassword = GenerateHash.hashMd5(username, password);
//        Employer employer = persist.getEmployerByUsernameAndPassword(username, hashedPassword);
        Employer employer = userService.getEmployerByUsernameAndPassword(username, hashedPassword);

        if (employer == null) {
            return new LoginResponse(Errors.ERROR_WRONG_CREDENTIALS, false,
                    "Wrong username or password", null, null);
        }
        //RefreshToken refreshToken = tokenService.createAndSaveToken(employer.getId(), UserType.EMPLOYER);
        String refreshToken = tokenService.createRefreshToken(employer);
        String accessToken = tokenService.createAccessToken(employer);

        return new LoginResponse(null, true, "Login successful", accessToken, refreshToken, employer.getUserType());
//        return new LoginResponse(null, true,
//                "Login successful", refreshToken.getToken(), UserType.EMPLOYER);
    }

}