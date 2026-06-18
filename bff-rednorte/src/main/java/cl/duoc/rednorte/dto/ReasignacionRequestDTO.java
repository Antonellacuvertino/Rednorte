package cl.duoc.rednorte.dto;

public record ReasignacionRequestDTO(
        Long citaId,
        String fechaNueva,
        String horaNueva,
        String motivo,
        String medicoResponsable) {
}
