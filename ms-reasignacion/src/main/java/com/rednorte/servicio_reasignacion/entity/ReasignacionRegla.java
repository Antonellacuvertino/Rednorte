package com.rednorte.servicio_reasignacion.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reasignacion_reglas")
public class ReasignacionRegla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String especialidad;

    @Column(nullable = false)
    private String reglaTipo; // TIEMPO_ESPERA, CAPACIDAD_MAXIMA, PRIORIDAD

    @Column(nullable = false)
    private String valor; // tiempo en minutos, capacidad máxima, etc.

    @Column(nullable = false)
    private boolean activa = true;

    @Column
    private LocalDateTime fechaCreacion;

    @Column
    private String descripcion;

    // Constructor vacío
    public ReasignacionRegla() {}

    // Constructor con parámetros
    public ReasignacionRegla(String especialidad, String reglaTipo, String valor, String descripcion) {
        this.especialidad = especialidad;
        this.reglaTipo = reglaTipo;
        this.valor = valor;
        this.descripcion = descripcion;
        this.fechaCreacion = LocalDateTime.now();
        this.activa = true;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getReglaTipo() {
        return reglaTipo;
    }

    public void setReglaTipo(String reglaTipo) {
        this.reglaTipo = reglaTipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
