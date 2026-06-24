package com.facundo.sistemabilletera.dto;

public record AuthResponse(
        String token,
        UserResponse user
) {

}
