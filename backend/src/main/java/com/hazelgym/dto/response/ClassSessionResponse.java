package com.hazelgym.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record ClassSessionResponse(
        Long id,
        Long gymClassId,
        String gymClassName,
        LocalDate fecha,
        LocalTime horaInicio,
        LocalTime horaFin
) {
}
