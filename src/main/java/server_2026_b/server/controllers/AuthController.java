package server_2026_b.server.controllers;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server_2026_b.server.requests.LoginRequest;
import server_2026_b.server.responses.LoginResponse;
import server_2026_b.server.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/loginEmployee")
    public LoginResponse loginEmployee(@RequestBody LoginRequest request) {
        // סיסמה לרישום עובדים
//        String hashedPassword = GenerateHash.hashMd5(request.getUsername(), request.getPassword());
//        System.out.println("the password: " + hashedPassword);
        return authService.loginEmployee(request.getUsername(), request.getPassword());
    }

    @PostMapping("/loginEmployer")
    public LoginResponse loginEmployer(@RequestBody LoginRequest request){
        // סיסמה לרישום מנהלים
//        String hashedPassword = GenerateHash.hashMd5(request.getUsername(), request.getPassword());
//        System.out.println("the password: " + hashedPassword);
        return authService.loginEmployer(request.getUsername(), request.getPassword());
    }


}
