package com.hazelgym.dto.response;

public record GymClassResponse(
        Long id,
        String nombre,
        String descripcion,
        Integer duracion,
        Long entrenadorId,
        String entrenadorNombre,
        Boolean activa
) {
}
