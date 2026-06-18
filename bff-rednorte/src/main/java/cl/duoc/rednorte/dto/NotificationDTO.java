package cl.duoc.rednorte.dto;

import java.time.Instant;

public record NotificationDTO(
        Long id,
        String eventType,
        String title,
        String message,
        boolean readFlag,
        Instant createdAt) {
}
