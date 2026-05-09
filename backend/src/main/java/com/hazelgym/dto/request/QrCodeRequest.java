package com.hazelgym.dto.request;

import com.hazelgym.model.QrType;

import jakarta.validation.constraints.NotNull;

public class QrCodeRequest {

    @NotNull
    private QrType tipo;

    @NotNull
    private Boolean esEntradaGimnasio;

    private Long maquinaId;

    private Long sesionClaseId;

    public QrType getTipo() {
        return tipo;
    }

    public void setTipo(QrType tipo) {
        this.tipo = tipo;
    }

    public Boolean getEsEntradaGimnasio() {
        return esEntradaGimnasio;
    }

    public void setEsEntradaGimnasio(Boolean esEntradaGimnasio) {
        this.esEntradaGimnasio = esEntradaGimnasio;
    }

    public Long getMaquinaId() {
        return maquinaId;
    }

    public void setMaquinaId(Long maquinaId) {
        this.maquinaId = maquinaId;
    }

    public Long getSesionClaseId() {
        return sesionClaseId;
    }

    public void setSesionClaseId(Long sesionClaseId) {
        this.sesionClaseId = sesionClaseId;
    }
}
