package com.hazelgym.dto.response;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String nombre,
        String email,
        String role,
        Boolean activo,
        LocalDateTime fechaCreacion
) {
}
