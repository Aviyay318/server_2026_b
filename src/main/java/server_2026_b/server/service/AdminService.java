package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.AdminRepository;
import server_2026_b.server.entities.User;
import server_2026_b.server.responses.AdminResponse;
import server_2026_b.server.utils.Errors;
import server_2026_b.server.utils.UserType;




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

    public AdminResponse getAllUsers(String token) {
        if (validateAdmin(token) == null) {
            return new AdminResponse(false, Errors.ERROR_INVALID_TOKEN, "Unauthorized");
        }
        return new AdminResponse(true, null, adminRepository.findAllGeneralUsers());
    }

    public AdminResponse getAllEmployers(String token) {
        if (validateAdmin(token) == null) {
            return new AdminResponse(false, Errors.ERROR_INVALID_TOKEN, "Unauthorized");
        }
        return new AdminResponse(true, null, adminRepository.findAllEmployers());
    }

    public AdminResponse getEmployeesByEmployerId(String token, Long employerId) {
        if (validateAdmin(token) == null) {
            return new AdminResponse(false, Errors.ERROR_INVALID_TOKEN, "Unauthorized");
        }
        return new AdminResponse(true, null, adminRepository.findEmployeesByEmployerId(employerId));
    }

    public AdminResponse getActiveUsers(String token) {
        if (validateAdmin(token) == null) {
            return new AdminResponse(false, Errors.ERROR_INVALID_TOKEN, "Unauthorized");
        }
        return new AdminResponse(true, null, adminRepository.findAllActiveUsers());
    }
}
