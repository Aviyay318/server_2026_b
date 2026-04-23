package server_2026_b.server.controllers;

import server_2026_b.server.entities.User;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.dto.UserDto;
import server_2026_b.server.service.Persist;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final Persist persist;

    public UserController(Persist persist) {
        this.persist = persist;
    }

    @PostMapping
    public BasicResponse createUser(@RequestBody UserDto userDto) {
        User user = new User(userDto.getUsername(), userDto.getPassword(), userDto.getRoleType());
        persist.save(user);
        return new BasicResponse(true, 0);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return persist.loadList(User.class);
    }


}
