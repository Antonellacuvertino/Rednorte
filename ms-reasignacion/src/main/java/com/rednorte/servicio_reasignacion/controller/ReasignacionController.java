package com.rednorte.servicio_reasignacion.controller;

import com.rednorte.servicio_reasignacion.dto.ReasignacionRequest;
import com.rednorte.servicio_reasignacion.entity.Reasignacion;
import com.rednorte.servicio_reasignacion.service.ReasignacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Expone la reasignacion manual y el historial trazable de cambios.
 */
@RestController
@RequestMapping("/api/reasignaciones")
@Tag(name = "Reasignaciones", description = "Reprogramacion manual de citas atrasadas")
public class ReasignacionController {
    private final ReasignacionService service;

    public ReasignacionController(ReasignacionService service) {
        this.service = service;
    }

    /**
     * @return historial de reasignaciones
     */
    @GetMapping
    @Operation(summary = "Listar historial de reasignaciones")
    public List<Reasignacion> findAll() {
        return service.findAll();
    }

    /**
     * Reprograma una cita y registra quien realizo el cambio.
     *
     * @param request nueva hora, motivo y medico responsable
     * @return trazabilidad creada
     */
    @PostMapping
    @Operation(summary = "Reasignar manualmente una cita")
    public ResponseEntity<Reasignacion> reprogramar(@RequestBody ReasignacionRequest request) {
        return ResponseEntity.ok(service.reprogramar(request));
    }
}
