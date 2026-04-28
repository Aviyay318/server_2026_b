package server_2026_b.server.service;
import org.springframework.stereotype.Service;
import server_2026_b.server.entities.AuthToken;
import server_2026_b.server.entities.Employee;
import server_2026_b.server.entities.Employer;
import server_2026_b.server.responses.ResponseDto;
import server_2026_b.server.utils.Errors;
import server_2026_b.server.utils.GenerateHash;
import server_2026_b.server.utils.UserType;
import static server_2026_b.server.utils.Errors.*;


@Service
public class AuthService { // מחוברת לדאטה בייס ע״י Persist
    private final Persist persist;
    private final TokenService tokenService;

    public AuthService(Persist persist, TokenService tokenService) {
        this.persist = persist;
        this.tokenService = tokenService;
    }

    public ResponseDto loginEmployee(String username, String password) {
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return new ResponseDto(ERROR_EMPTY_FIELD, false, "Missing username or password", null, null);
        }
        username = username.trim();
        password = password.trim();
        String hashedPassword = GenerateHash.hashMd5(username, password);
        Employee employee = persist.getEmployeeByUsernameAndPassword(username, hashedPassword);
        if (employee == null) {
            return new ResponseDto(ERROR_WRONG_CREDENTIALS, false, "Wrong username or password", null, null);
        }
        AuthToken authToken = tokenService.createAndSaveToken(employee.getId(), UserType.EMPLOYEE);
        return new ResponseDto(null, true, "Login successful", authToken.getToken(), UserType.EMPLOYEE);
    }

    public ResponseDto loginEmployer(String username, String password) {
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return new ResponseDto(Errors.ERROR_EMPTY_FIELD, false,
                    "Missing username or password", null, null);
        }
        username = username.trim();
        password = password.trim();
        String hashedPassword = GenerateHash.hashMd5(username, password);
        Employer employer = persist.getEmployerByUsernameAndPassword(username, hashedPassword);
        if (employer == null) {
            return new ResponseDto(Errors.ERROR_WRONG_CREDENTIALS, false,
                    "Wrong username or password", null, null);
        }
        AuthToken authToken = tokenService.createAndSaveToken(employer.getId(), UserType.EMPLOYER);
        return new ResponseDto(null, true,
                "Login successful", authToken.getToken(), UserType.EMPLOYER);
    }

}