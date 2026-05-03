package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.UserRepository;
import server_2026_b.server.entities.Employee;
import server_2026_b.server.entities.Employer;
import server_2026_b.server.entities.User;
import server_2026_b.server.utils.UserType;

@Service
public class UserService {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    public UserService(TokenService tokenService,
                       UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    private User getUserByAccessToken(String accessToken) {
        Long userId = tokenService.getUserIdFromAccessToken(accessToken);
        if (userId == null) {
            return null;
        }
        return userRepository.findUserById(userId);
    }

    public Employee getEmployeeByAccessToken(String accessToken) {
       User user = getUserByAccessToken(accessToken);
       if (user ==null){return null;}
        if (user.getUserType()!= UserType.EMPLOYEE) {
            return null;
        }
        return userRepository.findEmployeeById(user.getId());
    }

    public Employer getEmployerByAccessToken(String accessToken) {
        User user = getUserByAccessToken(accessToken);
        if (user ==null){return null;}
        if (user.getUserType() != UserType.EMPLOYER) {
            return null;
        }
        return userRepository.findEmployerById(user.getId());
    }


    public Employee getEmployeeByUsernameAndPassword(String username, String hashedPassword) {
        if (isMissing(username, hashedPassword)) {
            return null;
        }
        return userRepository.findEmployeeByUsernameAndPassword(username.trim(), hashedPassword.trim()
        );
    }


    public Employer getEmployerByUsernameAndPassword(String username, String hashedPassword) {
        if (isMissing(username, hashedPassword)) {
            return null;
        }
        return userRepository.findEmployerByUsernameAndPassword(
                username.trim(),
                hashedPassword.trim()
        );
    }

    private boolean isMissing(String username, String password) {
        return username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty();
    }
}