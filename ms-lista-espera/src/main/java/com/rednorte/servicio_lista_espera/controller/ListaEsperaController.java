package com.rednorte.servicio_lista_espera.controller;

import com.rednorte.servicio_lista_espera.entity.ListaEspera;
import com.rednorte.servicio_lista_espera.repository.ListaEsperaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lista-espera")
@CrossOrigin(origins = "*")
public class ListaEsperaController {

    @Autowired
    private ListaEsperaRepository listaEsperaRepository;

    @GetMapping
    public ResponseEntity<List<ListaEspera>> getAllListaEspera() {
        List<ListaEspera> lista = listaEsperaRepository.findAll();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<ListaEspera>> getListaEsperaPendiente() {
        List<ListaEspera> lista = listaEsperaRepository.findPendientesOrdenadasPorPrioridad();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/pendientes/{especialidad}")
    public ResponseEntity<List<ListaEspera>> getListaEsperaPendientePorEspecialidad(@PathVariable String especialidad) {
        List<ListaEspera> lista = listaEsperaRepository.findPendientesPorEspecialidadOrdenadasPorPrioridad(especialidad);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<ListaEspera>> getListaEsperaPorPaciente(@PathVariable Long pacienteId) {
        List<ListaEspera> lista = listaEsperaRepository.findByPacienteId(pacienteId);
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<ListaEspera> crearListaEspera(@RequestBody ListaEspera listaEspera) {
        ListaEspera nuevaListaEspera = listaEsperaRepository.save(listaEspera);
        return ResponseEntity.ok(nuevaListaEspera);
    }

    @PutMapping("/{id}/atender")
    public ResponseEntity<ListaEspera> atenderListaEspera(@PathVariable Long id) {
        Optional<ListaEspera> optionalListaEspera = listaEsperaRepository.findById(id);
        if (optionalListaEspera.isPresent()) {
            ListaEspera listaEspera = optionalListaEspera.get();
            listaEspera.setEstado("ATENDIDO");
            listaEspera.setFechaAtencion(java.time.LocalDateTime.now());
            ListaEspera updatedListaEspera = listaEsperaRepository.save(listaEspera);
            return ResponseEntity.ok(updatedListaEspera);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ListaEspera> cancelarListaEspera(@PathVariable Long id) {
        Optional<ListaEspera> optionalListaEspera = listaEsperaRepository.findById(id);
        if (optionalListaEspera.isPresent()) {
            ListaEspera listaEspera = optionalListaEspera.get();
            listaEspera.setEstado("CANCELADO");
            ListaEspera updatedListaEspera = listaEsperaRepository.save(listaEspera);
            return ResponseEntity.ok(updatedListaEspera);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarListaEspera(@PathVariable Long id) {
        if (listaEsperaRepository.existsById(id)) {
            listaEsperaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}