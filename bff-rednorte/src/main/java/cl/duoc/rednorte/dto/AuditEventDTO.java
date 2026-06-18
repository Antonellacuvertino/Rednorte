package cl.duoc.rednorte.dto;

import java.time.Instant;

public record AuditEventDTO(
        Long id,
        String eventType,
        Instant occurredAt,
        Instant receivedAt,
        String payload) {
}
