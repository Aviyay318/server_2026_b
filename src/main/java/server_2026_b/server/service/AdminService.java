package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.AdminRepository;
import server_2026_b.server.entities.User;
import server_2026_b.server.responses.AdminResponse;
import server_2026_b.server.responses.AdminUserDTO;
import server_2026_b.server.utils.Errors;
import server_2026_b.server.utils.UserType;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AdminService {
    private final UserService userService;
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository ,UserService userService) {
        this.adminRepository = adminRepository;
        this.userService = userService;
    }

    private User validateAdmin(String token) {
        User user = userService.getAdminByAccessToken(token);
        if (user != null && user.getUserType() == UserType.ADMIN) {
            return user;
        }
        return null;
    }

    // הופך רשימת User לרשימת AdminUserDTO
    private List<AdminUserDTO> convertToDtoList(List<User> users) {
        return users.stream()
                .map(AdminUserDTO::new)
                .collect(Collectors.toList());
    }

    public AdminResponse getAllUsers(String token) {
        if (validateAdmin(token) == null) {
            return new AdminResponse(false, Errors.ERROR_INVALID_TOKEN, "Unauthorized");
        }
        List<User> users = adminRepository.findAllGeneralUsers();
        return new AdminResponse(true, null, convertToDtoList(users));
    }

    public AdminResponse getAllEmployers(String token) {
        if (validateAdmin(token) == null) {
            return new AdminResponse(false, Errors.ERROR_INVALID_TOKEN, "Unauthorized");
        }
        List<User> employers = adminRepository.findAllEmployers();
        return new AdminResponse(true, null, convertToDtoList(employers));
    }

    public AdminResponse getEmployeesByEmployerId(String token, Long employerId) {
        if (validateAdmin(token) == null) {
            return new AdminResponse(false, Errors.ERROR_INVALID_TOKEN, "Unauthorized");
        }
        List<User> employees = adminRepository.findEmployeesByEmployerId(employerId);
        return new AdminResponse(true, null, convertToDtoList(employees));    }

    public AdminResponse getActiveUsers(String token) {
        if (validateAdmin(token) == null) {
            return new AdminResponse(false, Errors.ERROR_INVALID_TOKEN, "Unauthorized");
        }
        List<User> activeUsers = adminRepository.findAllActiveUsers();
        return new AdminResponse(true, null, convertToDtoList(activeUsers));    }
}
