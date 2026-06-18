package com.rednorte.servicio_notificaciones.controller;

import com.rednorte.servicio_notificaciones.service.NotificationService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NotificationControllerTest {
    @Test
    void respondeNotFoundCuandoNoExiste() {
        NotificationService service = mock(NotificationService.class);
        when(service.findAll(false)).thenReturn(List.of());
        when(service.markAsRead(9L)).thenThrow(new IllegalArgumentException());
        NotificationController controller = new NotificationController(service);

        assertEquals(0, controller.findAll(false).size());
        assertEquals(404, controller.markAsRead(9L).getStatusCode().value());
    }
}
