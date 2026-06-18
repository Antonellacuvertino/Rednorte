package com.rednorte.servicio_reasignacion.dto;

public record ReasignacionRequest(
        Long citaId,
        String fechaNueva,
        String horaNueva,
        String motivo,
        String medicoResponsable) {
}
