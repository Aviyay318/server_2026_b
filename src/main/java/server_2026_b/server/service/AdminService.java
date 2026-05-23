package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.AdminRepository;
import server_2026_b.server.entities.User;
import server_2026_b.server.requests.CreateEmployerRequest;
import server_2026_b.server.responses.AdminResponse;
import server_2026_b.server.responses.AdminUserDTO;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.utils.Errors;
import server_2026_b.server.utils.GenerateHash;
import server_2026_b.server.utils.IdValidator;
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


    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public BasicResponse createEmployer(String token, CreateEmployerRequest req) {
        if (validateAdmin(token) == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }
        if (isBlank(req.getPersonalId()) ||
                isBlank(req.getFirstName()) ||
                isBlank(req.getLastName())  ||
                isBlank(req.getPassword())) {
            return new BasicResponse(false, Errors.ERROR_EMPTY_FIELD);
        }
        if (!IdValidator.checkID(req.getPersonalId()))
            return new BasicResponse(false, Errors.ERROR_INVALID_ID);

        if (adminRepository.existsByPersonalId(req.getPersonalId())) {
            return new BasicResponse(false, Errors.ERROR_EMPLOYER_ALREADY_EXISTS);
        }
        User employer = new User(
                req.getPersonalId(),
                req.getFirstName(),
                req.getLastName(),
                GenerateHash.hashMd5(req.getPersonalId(), req.getPassword()),
                req.getPhone(),
                req.getEmail(),
                UserType.EMPLOYER
        );
        adminRepository.saveEmployer(employer);
        return new BasicResponse(true, null);
    }

    public BasicResponse deleteEmployer(String token, long employerId) {
        if (validateAdmin(token) == null) {
            return new BasicResponse(false, Errors.ERROR_INVALID_TOKEN);
        }
        User employer = adminRepository.findEmployerById(employerId);
        if (employer == null) {
            return new BasicResponse(false, Errors.ERROR_EMPLOYER_NOT_FOUND);
        }

        adminRepository.deleteAllRelationsForEmployer(employerId);
        adminRepository.deleteEmployer(employer);
        return new BasicResponse(true, null);
    }
}
