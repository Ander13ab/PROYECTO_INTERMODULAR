package com.hazelgym.dto.request;

import com.hazelgym.model.MachineStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MachineRequest {

    @NotBlank
    @Size(max = 100)
    private String nombre;

    private String descripcion;

    @Size(max = 100)
    private String grupoMuscular;

    private String instrucciones;

    @Size(max = 20)
    private String nivel;

    private String advertenciaSeguridad;

    @Size(max = 255)
    private String imagenUrl;

    @NotNull
    private MachineStatus estado;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getAdvertenciaSeguridad() {
        return advertenciaSeguridad;
    }

    public void setAdvertenciaSeguridad(String advertenciaSeguridad) {
        this.advertenciaSeguridad = advertenciaSeguridad;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public MachineStatus getEstado() {
        return estado;
    }

    public void setEstado(MachineStatus estado) {
        this.estado = estado;
    }
}
