package com.rednorte.servicio_auditoria.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rednorte.servicio_auditoria.entity.AuditEvent;
import com.rednorte.servicio_auditoria.repository.AuditEventRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class AuditEventService {
    private final AuditEventRepository repository;
    private final ObjectMapper objectMapper;

    public AuditEventService(AuditEventRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "rednorte.audit")
    public void receive(Map<String, Object> message) {
        String eventType = String.valueOf(message.getOrDefault("eventType", "DESCONOCIDO"));
        Instant occurredAt = parseInstant(message.get("occurredAt"));
        repository.save(new AuditEvent(eventType, occurredAt, Instant.now(), serialize(message.get("payload"))));
    }

    public List<AuditEvent> findAll(String eventType) {
        if (eventType == null || eventType.isBlank()) {
            return repository.findAllByOrderByOccurredAtDesc();
        }
        return repository.findByEventTypeOrderByOccurredAtDesc(eventType.toUpperCase());
    }

    private Instant parseInstant(Object value) {
        try {
            return value == null ? Instant.now() : Instant.parse(value.toString());
        } catch (RuntimeException ex) {
            return Instant.now();
        }
    }

    private String serialize(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException ex) {
            return String.valueOf(payload);
        }
    }
}
