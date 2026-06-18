package com.rednorte.servicio_reasignacion.dto;

public record CitaResponse(
        Long id,
        Long pacienteId,
        String fecha,
        String hora,
        String especialidad,
        String tipoCita,
        int prioridad) {
}
