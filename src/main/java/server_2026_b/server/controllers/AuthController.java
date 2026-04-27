package server_2026_b.server.controllers;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server_2026_b.server.requests.LoginRequest;
import server_2026_b.server.responses.ResponseDto;

@RestController
@RequestMapping("/auth")
public class AuthController {


//    @PostMapping("/employeeLogin")
//
//
//
    @PostMapping("/loginEmployer")
    public ResponseDto loginEmployer(@RequestBody LoginRequest request){
        return authService.loginEmployer(request.getUsername(), request.getPassword());
    }


}
