package server_2026_b.server.controllers;


import org.springframework.web.bind.annotation.*;
import server_2026_b.server.requests.LoginRequest;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.LoginResponse;
import server_2026_b.server.service.AuthService;
import server_2026_b.server.utils.GenerateHash;

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
        String hashedPassword = GenerateHash.hashMd5(request.getUsername(), request.getPassword());
        System.out.println("the password: " + hashedPassword);
        return authService.loginEmployee(request.getUsername(), request.getPassword());
    }

    @PostMapping("/loginEmployer")
    public LoginResponse loginEmployer(@RequestBody LoginRequest request){
        // סיסמה לרישום מנהלים
        String hashedPassword = GenerateHash.hashMd5(request.getUsername(), request.getPassword());
        System.out.println("the password: " + hashedPassword);
        return authService.loginEmployer(request.getUsername(), request.getPassword());
    }

    @RequestMapping("/logout")
    public BasicResponse logoutUser(@RequestHeader("Authorization") String token){
        // TODO: delete the token from table.
        return new BasicResponse();
    }
}
