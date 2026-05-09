package com.hazelgym.dto.response;

import com.hazelgym.model.QrType;

public record QrCodeResponse(
        Long id,
        QrType tipo,
        Boolean esEntradaGimnasio,
        Long maquinaId,
        String maquinaNombre,
        Long sesionClaseId,
        String sesionClaseResumen
) {
}
