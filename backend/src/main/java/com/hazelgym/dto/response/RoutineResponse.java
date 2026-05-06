package com.hazelgym.dto.response;

import java.time.LocalDateTime;

public record RoutineResponse(
        Long id,
        String nombre,
        String descripcion,
        Long entrenadorId,
        String entrenadorNombre,
        LocalDateTime fechaCreacion
) {
}
