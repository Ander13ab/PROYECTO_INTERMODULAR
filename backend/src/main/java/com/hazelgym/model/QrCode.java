package com.hazelgym.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "codigos_qr")
public class QrCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private QrType tipo;

    @Column(name = "es_entrada_gimnasio", nullable = false)
    private Boolean esEntradaGimnasio = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maquina_id")
    private Machine maquina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_clase_id")
    private ClassSession sesionClase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Machine getMaquina() {
        return maquina;
    }

    public void setMaquina(Machine maquina) {
        this.maquina = maquina;
    }

    public ClassSession getSesionClase() {
        return sesionClase;
    }

    public void setSesionClase(ClassSession sesionClase) {
        this.sesionClase = sesionClase;
    }
}
