package com.rednorte.servicio_notificaciones.controller;

import com.rednorte.servicio_notificaciones.entity.Notification;
import com.rednorte.servicio_notificaciones.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Expone las notificaciones operativas generadas por eventos RabbitMQ.
 */
@RestController
@RequestMapping("/api/notificaciones")
@Tag(name = "Notificaciones", description = "Consulta y lectura de avisos operativos")
public class NotificationController {
    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    /**
     * @param noLeidas limita la consulta a notificaciones pendientes
     * @return notificaciones solicitadas
     */
    @GetMapping
    @Operation(summary = "Listar notificaciones")
    public List<Notification> findAll(@RequestParam(defaultValue = "false") boolean noLeidas) {
        return service.findAll(noLeidas);
    }

    /**
     * @param id identificador de la notificacion
     * @return notificacion actualizada
     */
    @PutMapping("/{id}/leer")
    @Operation(summary = "Marcar notificacion como leida")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(service.markAsRead(id));
    }
}
