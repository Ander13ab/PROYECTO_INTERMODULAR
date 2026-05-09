package com.hazelgym.dto.response;

import java.time.LocalDateTime;

public record AttendanceResponse(
        Long id,
        Long usuarioId,
        String usuarioNombre,
        Long qrCodeId,
        String qrType,
        LocalDateTime fechaHora
) {
}
