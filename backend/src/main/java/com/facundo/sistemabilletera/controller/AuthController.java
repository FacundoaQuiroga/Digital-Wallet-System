package com.facundo.sistemabilletera.controller;
import com.facundo.sistemabilletera.dto.UserResponse;
import com.facundo.sistemabilletera.model.AppUser;
import com.facundo.sistemabilletera.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.facundo.sistemabilletera.dto.AuthResponse;
import com.facundo.sistemabilletera.service.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
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
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        AppUser user = userService.login(
                request.email(),
                request.password()
        );

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail()
        );

        String token = jwtService.generateToken(user.getEmail());
        
        return new AuthResponse(token, userResponse);

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
