package cl.duoc.rednorte.dto;

import java.time.LocalDateTime;

public record ReasignacionDTO(
        Long id,
        Long citaId,
        Long pacienteId,
        String fechaAnterior,
        String horaAnterior,
        String fechaNueva,
        String horaNueva,
        String motivo,
        String medicoResponsable,
        String estado,
        LocalDateTime fechaRegistro) {
}
