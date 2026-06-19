package com.rednorte.servicio_auditoria.controller;

import com.rednorte.servicio_auditoria.entity.AuditEvent;
import com.rednorte.servicio_auditoria.service.AuditEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Permite consultar la trazabilidad persistida desde RabbitMQ.
 */
@RestController
@RequestMapping("/api/auditoria")
@Tag(name = "Auditoria", description = "Consulta de eventos operativos")
public class AuditEventController {
    private final AuditEventService service;

    public AuditEventController(AuditEventService service) {
        this.service = service;
    }

    /**
     * @param tipo filtro opcional de tipo de evento
     * @return eventos ordenados por fecha
     */
    @GetMapping
    @Operation(summary = "Listar eventos de auditoria")
    public List<AuditEvent> findAll(@RequestParam(required = false) String tipo) {
        return service.findAll(tipo);
    }

    /**
     * @param id identificador del evento
     * @return evento encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar evento por id")
    public AuditEvent findById(@PathVariable Long id) {
        return service.findById(id);
    }
}
