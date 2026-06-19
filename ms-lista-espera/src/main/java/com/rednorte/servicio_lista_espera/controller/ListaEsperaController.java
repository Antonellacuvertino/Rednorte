package com.rednorte.servicio_lista_espera.controller;

import java.util.List;

import com.rednorte.servicio_lista_espera.entity.ListaEspera;
import com.rednorte.servicio_lista_espera.service.ListaEsperaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone las operaciones de priorizacion y atencion de la lista de espera.
 */
@RestController
@RequestMapping("/api/lista-espera")
@Tag(name = "Lista de espera", description = "Priorizacion de pacientes pendientes")
public class ListaEsperaController {

    private final ListaEsperaService service;

    public ListaEsperaController(ListaEsperaService service) {
        this.service = service;
    }

    /** @return todos los registros de espera */
    @GetMapping
    @Operation(summary = "Listar registros")
    public ResponseEntity<List<ListaEspera>> getAllListaEspera() {
        return ResponseEntity.ok(service.listar());
    }

    /** @return registros pendientes ordenados por prioridad */
    @GetMapping("/pendientes")
    @Operation(summary = "Listar pendientes por prioridad")
    public ResponseEntity<List<ListaEspera>> getListaEsperaPendiente() {
        return ResponseEntity.ok(service.listarPendientes());
    }

    /**
     * @param especialidad especialidad clinica
     * @return pendientes de la especialidad
     */
    @GetMapping("/pendientes/{especialidad}")
    @Operation(summary = "Listar pendientes por especialidad")
    public ResponseEntity<List<ListaEspera>> getListaEsperaPendientePorEspecialidad(
            @PathVariable String especialidad) {
        return ResponseEntity.ok(service.listarPendientesPorEspecialidad(especialidad));
    }

    /**
     * @param pacienteId identificador del paciente
     * @return registros asociados
     */
    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar espera por paciente")
    public ResponseEntity<List<ListaEspera>> getListaEsperaPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.listarPorPaciente(pacienteId));
    }

    /**
     * @param listaEspera datos del nuevo registro
     * @return registro creado
     */
    @PostMapping
    @Operation(summary = "Agregar paciente a lista de espera")
    public ResponseEntity<ListaEspera> crearListaEspera(@RequestBody ListaEspera listaEspera) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(listaEspera));
    }

    /**
     * @param id identificador del registro
     * @return registro marcado como atendido
     */
    @PutMapping("/{id}/atender")
    @Operation(summary = "Marcar registro como atendido")
    public ResponseEntity<ListaEspera> atenderListaEspera(@PathVariable Long id) {
        return ResponseEntity.ok(service.atender(id));
    }

    /**
     * @param id identificador del registro
     * @return registro cancelado
     */
    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar registro")
    public ResponseEntity<ListaEspera> cancelarListaEspera(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }

    /**
     * @param id identificador del registro
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar registro")
    public ResponseEntity<Void> eliminarListaEspera(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
