package com.hazelgym.dto.response;

import java.time.LocalDateTime;

public record RoutineAssignmentResponse(
        Long id,
        Long routineId,
        String routineName,
        Long clientId,
        String clientName,
        LocalDateTime fechaAsignacion
) {
}
