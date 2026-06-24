package com.facundo.sistemabilletera.controller;
import com.facundo.sistemabilletera.dto.UserResponse;
import com.facundo.sistemabilletera.model.AppUser;
import com.facundo.sistemabilletera.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
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

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginRequest request) {
        AppUser user = userService.login(
                request.email(),
                request.password()
        );

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail()
        );
    }

    public record RegisterRequest(
            @NotBlank String fullName,
            @NotBlank @Email String email,
            @NotBlank String password
    ) {
    }

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String password
    ) {
    }
    
}
