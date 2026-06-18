package com.rednorte.servicio_reasignacion.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "reasignaciones")
public class Reasignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long citaId;
    private Long pacienteId;
    private String fechaAnterior;
    private String horaAnterior;
    private String fechaNueva;
    private String horaNueva;
    private String motivo;
    private String medicoResponsable;
    private String estado;
    private LocalDateTime fechaRegistro;

    @PrePersist
    void prePersist() {
        fechaRegistro = LocalDateTime.now();
        estado = "CONFIRMADA";
    }

    public Long getId() { return id; }
    public Long getCitaId() { return citaId; }
    public Long getPacienteId() { return pacienteId; }
    public String getFechaAnterior() { return fechaAnterior; }
    public String getHoraAnterior() { return horaAnterior; }
    public String getFechaNueva() { return fechaNueva; }
    public String getHoraNueva() { return horaNueva; }
    public String getMotivo() { return motivo; }
    public String getMedicoResponsable() { return medicoResponsable; }
    public String getEstado() { return estado; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setId(Long id) { this.id = id; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public void setFechaAnterior(String fechaAnterior) { this.fechaAnterior = fechaAnterior; }
    public void setHoraAnterior(String horaAnterior) { this.horaAnterior = horaAnterior; }
    public void setFechaNueva(String fechaNueva) { this.fechaNueva = fechaNueva; }
    public void setHoraNueva(String horaNueva) { this.horaNueva = horaNueva; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public void setMedicoResponsable(String medicoResponsable) { this.medicoResponsable = medicoResponsable; }
}
