package com.facundo.sistemabilletera.controller;

import com.facundo.sistemabilletera.model.AppUser;
import com.facundo.sistemabilletera.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppUser createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(
                request.fullName(),
                request.email(),
                request.password()
        );
    }

    public record CreateUserRequest(
            String fullName,
            String email,
            String password
    ) {
    }
}
