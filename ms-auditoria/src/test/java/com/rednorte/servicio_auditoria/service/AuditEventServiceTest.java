package com.rednorte.servicio_auditoria.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rednorte.servicio_auditoria.entity.AuditEvent;
import com.rednorte.servicio_auditoria.repository.AuditEventRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuditEventServiceTest {
    @Test
    void recibeYPersisteEvento() {
        AuditEventRepository repository = mock(AuditEventRepository.class);
        AuditEventService service = new AuditEventService(repository, new ObjectMapper());

        service.receive(Map.of(
                "eventType", "PACIENTE_CREADO",
                "occurredAt", "2026-06-18T12:00:00Z",
                "payload", Map.of("id", 1)));

        verify(repository).save(any(AuditEvent.class));
    }

    @Test
    void filtraPorTipoOListaTodos() {
        AuditEventRepository repository = mock(AuditEventRepository.class);
        AuditEventService service = new AuditEventService(repository, new ObjectMapper());
        when(repository.findAllByOrderByOccurredAtDesc()).thenReturn(List.of());
        when(repository.findByEventTypeOrderByOccurredAtDesc("CITA_CREADA")).thenReturn(List.of());

        assertEquals(0, service.findAll(null).size());
        assertEquals(0, service.findAll("cita_creada").size());
    }
}
