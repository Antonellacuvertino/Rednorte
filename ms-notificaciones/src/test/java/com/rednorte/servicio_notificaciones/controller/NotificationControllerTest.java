package com.rednorte.servicio_notificaciones.controller;

import com.rednorte.servicio_notificaciones.entity.Notification;
import com.rednorte.servicio_notificaciones.exception.ResourceNotFoundException;
import com.rednorte.servicio_notificaciones.service.NotificationService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NotificationControllerTest {
    @Test
    void listaMarcaComoLeidaYPropagaNoEncontrado() {
        NotificationService service = mock(NotificationService.class);
        Notification notification = mock(Notification.class);
        when(service.findAll(false)).thenReturn(List.of());
        when(service.markAsRead(1L)).thenReturn(notification);
        when(service.markAsRead(9L)).thenThrow(new ResourceNotFoundException("No existe"));
        NotificationController controller = new NotificationController(service);

        assertEquals(0, controller.findAll(false).size());
        assertEquals(notification, controller.markAsRead(1L).getBody());
        assertThrows(ResourceNotFoundException.class, () -> controller.markAsRead(9L));
    }
}
