package com.hazelgym.dto.request;

import jakarta.validation.constraints.NotNull;

public class AttendanceRequest {

    @NotNull
    private Long usuarioId;

    @NotNull
    private Long qrCodeId;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(Long qrCodeId) {
        this.qrCodeId = qrCodeId;
    }
}
