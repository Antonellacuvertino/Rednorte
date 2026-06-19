package com.rednorte.servicio_notificaciones.service;

import com.rednorte.servicio_notificaciones.entity.Notification;
import com.rednorte.servicio_notificaciones.exception.ResourceNotFoundException;
import com.rednorte.servicio_notificaciones.repository.NotificationRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationServiceTest {
    @Test
    void creaNotificacionesParaTodosLosTiposDeEvento() {
        NotificationRepository repository = mock(NotificationRepository.class);
        NotificationService service = new NotificationService(repository);

        service.receive(Map.of("eventType", "PACIENTE_CREADO"));
        service.receive(Map.of("eventType", "CITA_CREADA"));
        service.receive(Map.of("eventType", "CITA_REPROGRAMADA"));
        service.receive(Map.of("eventType", "OTRO"));
        service.receive(Map.of());

        verify(repository, org.mockito.Mockito.times(5)).save(any(Notification.class));
    }

    @Test
    void listaTodasONoLeidas() {
        NotificationRepository repository = mock(NotificationRepository.class);
        NotificationService service = new NotificationService(repository);
        when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of());
        when(repository.findByReadFlagFalseOrderByCreatedAtDesc()).thenReturn(List.of());

        assertEquals(0, service.findAll(false).size());
        assertEquals(0, service.findAll(true).size());
    }

    @Test
    void marcaComoLeidaYRechazaIdInexistente() {
        NotificationRepository repository = mock(NotificationRepository.class);
        Notification notification = new Notification("CITA_CREADA", "Nueva cita", "Mensaje");
        when(repository.findById(1L)).thenReturn(Optional.of(notification));
        when(repository.save(notification)).thenReturn(notification);
        NotificationService service = new NotificationService(repository);

        assertTrue(service.markAsRead(1L).isReadFlag());
        assertThrows(ResourceNotFoundException.class, () -> service.markAsRead(2L));
    }
}
