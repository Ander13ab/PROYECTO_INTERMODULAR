package com.hazelgym.dto.response;

public record AuthenticatedUserResponse(
        Long id,
        String nombre,
        String email,
        String role,
        Boolean activo
) {
}
