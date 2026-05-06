package com.hazelgym.dto.response;

import com.hazelgym.model.MachineStatus;

public record MachineResponse(
        Long id,
        String nombre,
        String descripcion,
        String grupoMuscular,
        String instrucciones,
        String nivel,
        String advertenciaSeguridad,
        String imagenUrl,
        MachineStatus estado
) {
}
