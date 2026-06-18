package com.rednorte.servicio_reasignacion.controller;

import com.rednorte.servicio_reasignacion.dto.ReasignacionRequest;
import com.rednorte.servicio_reasignacion.entity.Reasignacion;
import com.rednorte.servicio_reasignacion.service.ReasignacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reasignaciones")
public class ReasignacionController {
    private final ReasignacionService service;

    public ReasignacionController(ReasignacionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Reasignacion> findAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Reasignacion> reprogramar(@RequestBody ReasignacionRequest request) {
        try {
            return ResponseEntity.ok(service.reprogramar(request));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
