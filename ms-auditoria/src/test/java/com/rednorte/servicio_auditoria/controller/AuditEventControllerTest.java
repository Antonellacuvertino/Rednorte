package com.rednorte.servicio_auditoria.controller;

import com.rednorte.servicio_auditoria.service.AuditEventService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuditEventControllerTest {
    @Test
    void entregaEventos() {
        AuditEventService service = mock(AuditEventService.class);
        when(service.findAll(null)).thenReturn(List.of());

        assertTrue(new AuditEventController(service).findAll(null).isEmpty());
    }
}
