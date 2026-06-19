package com.rednorte.servicio_auditoria.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rednorte.servicio_auditoria.entity.AuditEvent;
import com.rednorte.servicio_auditoria.exception.ResourceNotFoundException;
import com.rednorte.servicio_auditoria.repository.AuditEventRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuditEventServiceTest {

    @Test
    void recibeYPersisteEventosConFechasValidasOInvalidas() {
        AuditEventRepository repository = mock(AuditEventRepository.class);
        AuditEventService service = new AuditEventService(repository, new ObjectMapper());

        service.receive(Map.of(
                "eventType", "PACIENTE_CREADO",
                "occurredAt", "2026-06-18T12:00:00Z",
                "payload", Map.of("id", 1)));
        service.receive(Map.of("occurredAt", "fecha-invalida"));

        Map<String, Object> withoutDate = new HashMap<>();
        withoutDate.put("payload", "sin fecha");
        service.receive(withoutDate);

        verify(repository, org.mockito.Mockito.times(3)).save(any(AuditEvent.class));
    }

    @Test
    void serializaConFallbackYConsultaFiltros() throws JsonProcessingException {
        AuditEventRepository repository = mock(AuditEventRepository.class);
        ObjectMapper mapper = mock(ObjectMapper.class);
        when(mapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("falla") { });
        AuditEventService service = new AuditEventService(repository, mapper);
        when(repository.findAllByOrderByOccurredAtDesc()).thenReturn(List.of());
        when(repository.findByEventTypeOrderByOccurredAtDesc("CITA_CREADA")).thenReturn(List.of());

        service.receive(Map.of("eventType", "CITA_CREADA", "payload", "texto"));

        assertEquals(0, service.findAll(null).size());
        assertEquals(0, service.findAll(" ").size());
        assertEquals(0, service.findAll("cita_creada").size());
    }

    @Test
    void buscaEventoYRechazaIdInexistente() {
        AuditEventRepository repository = mock(AuditEventRepository.class);
        AuditEvent event = mock(AuditEvent.class);
        when(repository.findById(1L)).thenReturn(Optional.of(event));
        when(repository.findById(99L)).thenReturn(Optional.empty());
        AuditEventService service = new AuditEventService(repository, new ObjectMapper());

        assertEquals(event, service.findById(1L));
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }
}
