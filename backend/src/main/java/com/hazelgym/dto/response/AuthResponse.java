package com.hazelgym.dto.response;

public record AuthResponse(
        String token,
        String tokenType,
        Long userId,
        String nombre,
        String email,
        String role
) {
}
