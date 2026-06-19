package com.rednorte.servicio_auditoria.controller;

import java.util.List;

import com.rednorte.servicio_auditoria.entity.AuditEvent;
import com.rednorte.servicio_auditoria.service.AuditEventService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuditEventControllerTest {

    @Test
    void entregaListaYEventoIndividual() {
        AuditEventService service = mock(AuditEventService.class);
        AuditEvent event = mock(AuditEvent.class);
        when(service.findAll(null)).thenReturn(List.of());
        when(service.findById(1L)).thenReturn(event);
        AuditEventController controller = new AuditEventController(service);

        assertTrue(controller.findAll(null).isEmpty());
        assertEquals(event, controller.findById(1L));
    }
}
