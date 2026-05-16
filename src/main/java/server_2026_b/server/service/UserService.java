package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.UserRepository;
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
    public User getUserByAccessTokenAndType(String accessToken, UserType userType) {
        if (userType == null) {
            return null;
        }
        User user = getUserByAccessToken(accessToken);
        if (user == null|| user.getUserType() != userType) {
            return null;
        }

        return user;
    }


    public User getUserByUsernameAndPasswordAndType(String username, String hashedPassword,  UserType userType) {
        if (isMissing(username, hashedPassword)|| userType == null) {
            return null;
        }
        return userRepository.findUserByUsernamePasswordAndType(username.trim(), hashedPassword.trim(), userType);
    }




    private boolean isMissing(String username, String password) {
        return username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty();
    }

    public User getEmployeeByAccessToken(String accessToken) {
        return getUserByAccessTokenAndType(accessToken, UserType.EMPLOYEE);
    }

    public User getEmployerByAccessToken(String accessToken) {
        return getUserByAccessTokenAndType(accessToken, UserType.EMPLOYER);
    }
}
