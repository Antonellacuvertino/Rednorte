package com.RedNorte.servicio_citas.controller;

import java.util.List;

import com.RedNorte.servicio_citas.model.Cita;
import com.RedNorte.servicio_citas.service.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone la agenda medica y la reprogramacion de citas.
 */
@RestController
@RequestMapping("/api/citas")
@Tag(name = "Citas", description = "Agenda y reprogramacion de citas medicas")
public class CitaController {

    private final CitaService service;

    public CitaController(CitaService service) {
        this.service = service;
    }

    /**
     * Lista todas las citas.
     *
     * @return citas registradas
     */
    @GetMapping
    @Operation(summary = "Listar citas")
    public List<Cita> listar() {
        return service.listar();
    }

    /**
     * Lista las citas asociadas a un paciente.
     *
     * @param pacienteId identificador del paciente
     * @return citas del paciente
     */
    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar citas por paciente")
    public List<Cita> listarPorPaciente(@PathVariable Long pacienteId) {
        return service.listarPorPaciente(pacienteId);
    }

    /**
     * Obtiene una cita por id.
     *
     * @param id identificador de la cita
     * @return cita encontrada
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar cita por id")
    public ResponseEntity<Cita> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /**
     * Agenda una cita aplicando la prioridad definida por su tipo.
     *
     * @param tipo tipo de cita
     * @param datosCita datos de agenda
     * @return cita creada
     */
    @PostMapping("/{tipo}")
    @Operation(summary = "Agendar cita")
    public ResponseEntity<Cita> agendar(@PathVariable String tipo, @RequestBody Cita datosCita) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.agendar(tipo, datosCita));
    }

    /**
     * Cambia la fecha y hora de una cita.
     *
     * @param id identificador de la cita
     * @param cambios nueva fecha y hora
     * @return cita actualizada
     */
    @PutMapping("/{id}/reprogramar")
    @Operation(summary = "Reprogramar cita")
    public ResponseEntity<Cita> reprogramar(@PathVariable Long id, @RequestBody Cita cambios) {
        return ResponseEntity.ok(service.reprogramar(id, cambios));
    }
}
