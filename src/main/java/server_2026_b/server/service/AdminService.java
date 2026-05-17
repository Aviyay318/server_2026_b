package server_2026_b.server.service;

import org.springframework.stereotype.Service;
import server_2026_b.server.database.AdminRepository;
import server_2026_b.server.entities.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<User> getAllUsers() {
        return adminRepository.findAllGeneralUsers();
    }

    public List<User> getAllEmployers() {
        return adminRepository.findAllEmployers();
    }

    public List<User> getEmployeesByEmployerId(Long employerId) {
        return adminRepository.findEmployeesByEmployerId(employerId);
    }

    public List<User> getActiveUsers() {
        return adminRepository.findAllActiveUsers();
    }
}
