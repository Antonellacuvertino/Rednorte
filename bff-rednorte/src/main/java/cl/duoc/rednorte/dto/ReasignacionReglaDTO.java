package cl.duoc.rednorte.dto;

import java.time.LocalDateTime;

public class ReasignacionReglaDTO {
    private Long id;
    private String especialidad;
    private String reglaTipo;
    private String valor;
    private boolean activa = true;
    private LocalDateTime fechaCreacion;
    private String descripcion;

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
