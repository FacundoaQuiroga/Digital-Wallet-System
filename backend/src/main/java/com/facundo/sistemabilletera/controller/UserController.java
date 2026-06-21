package com.facundo.sistemabilletera.controller;

import com.facundo.sistemabilletera.model.AppUser;
import com.facundo.sistemabilletera.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.facundo.sistemabilletera.dto.UserResponse;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        AppUser user = userService.createUser(
                request.fullName(),
                request.email(),
                request.password()
        );

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail()
        );
    }

    public record CreateUserRequest(
            String fullName,
            String email,
            String password
    ) {
    }
}
