package com.rednorte.servicio_notificaciones.service;

import com.rednorte.servicio_notificaciones.entity.Notification;
import com.rednorte.servicio_notificaciones.repository.NotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NotificationService {
    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "rednorte.notifications")
    public void receive(Map<String, Object> event) {
        String type = String.valueOf(event.getOrDefault("eventType", "EVENTO"));
        repository.save(new Notification(type, titleFor(type), messageFor(type)));
    }

    public List<Notification> findAll(boolean unreadOnly) {
        return unreadOnly
                ? repository.findByReadFlagFalseOrderByCreatedAtDesc()
                : repository.findAllByOrderByCreatedAtDesc();
    }

    public Notification markAsRead(Long id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notificacion no encontrada"));
        notification.setReadFlag(true);
        return repository.save(notification);
    }

    private String titleFor(String type) {
        return switch (type) {
            case "PACIENTE_CREADO" -> "Nuevo paciente";
            case "CITA_CREADA" -> "Nueva cita";
            case "CITA_REPROGRAMADA" -> "Cita reprogramada";
            default -> "Actividad del sistema";
        };
    }

    private String messageFor(String type) {
        return switch (type) {
            case "PACIENTE_CREADO" -> "Se registro un nuevo paciente en RedNorte.";
            case "CITA_CREADA" -> "Se agendo una nueva cita medica.";
            case "CITA_REPROGRAMADA" -> "Un medico asigno un nuevo horario a una cita.";
            default -> "Se recibio el evento " + type + ".";
        };
    }
}
