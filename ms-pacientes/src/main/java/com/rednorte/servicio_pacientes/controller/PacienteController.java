package com.rednorte.servicio_pacientes.controller;

import java.util.List;

import com.rednorte.servicio_pacientes.exception.ResourceNotFoundException;
import com.rednorte.servicio_pacientes.model.Paciente;
import com.rednorte.servicio_pacientes.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone las operaciones REST para gestionar pacientes en RedNorte.
 */
@RestController
@RequestMapping("/api/pacientes")
@Tag(name = "Pacientes", description = "Registro y consulta de pacientes")
public class PacienteController {

    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    /**
     * Obtiene todos los pacientes registrados.
     *
     * @return pacientes disponibles
     */
    @GetMapping
    @Operation(summary = "Listar pacientes")
    public ResponseEntity<List<Paciente>> listar() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    /**
     * Registra un paciente.
     *
     * @param paciente datos que se almacenaran
     * @return paciente creado
     */
    @PostMapping
    @Operation(summary = "Crear paciente")
    public ResponseEntity<Paciente> crear(@RequestBody Paciente paciente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(paciente));
    }

    /**
     * Busca un paciente por su identificador interno.
     *
     * @param id identificador del paciente
     * @return paciente encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por id")
    public ResponseEntity<Paciente> buscarPorId(@PathVariable Long id) {
        Paciente paciente = service.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado: " + id));
        return ResponseEntity.ok(paciente);
    }

    /**
     * Busca un paciente por su RUT.
     *
     * @param rut RUT del paciente
     * @return paciente encontrado
     */
    @GetMapping("/rut/{rut}")
    @Operation(summary = "Buscar paciente por RUT")
    public ResponseEntity<Paciente> buscarPorRut(@PathVariable String rut) {
        Paciente paciente = service.obtenerPorRut(rut);
        if (paciente == null) {
            throw new ResourceNotFoundException("Paciente no encontrado para RUT: " + rut);
        }
        return ResponseEntity.ok(paciente);
    }
}
