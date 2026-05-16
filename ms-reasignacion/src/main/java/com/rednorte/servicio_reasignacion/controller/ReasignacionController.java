package com.rednorte.servicio_reasignacion.controller;

import com.rednorte.servicio_reasignacion.entity.ReasignacionRegla;
import com.rednorte.servicio_reasignacion.repository.ReasignacionReglaRepository;
import com.rednorte.servicio_reasignacion.service.ReasignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reasignacion")
@CrossOrigin(origins = "*")
public class ReasignacionController {

    @Autowired
    private ReasignacionReglaRepository reglaRepository;

    @Autowired
    private ReasignacionService reasignacionService;

    @GetMapping("/reglas")
    public ResponseEntity<List<ReasignacionRegla>> getAllReglas() {
        List<ReasignacionRegla> reglas = reglaRepository.findAll();
        return ResponseEntity.ok(reglas);
    }

    @GetMapping("/reglas/activas")
    public ResponseEntity<List<ReasignacionRegla>> getReglasActivas() {
        List<ReasignacionRegla> reglas = reglaRepository.findByActiva(true);
        return ResponseEntity.ok(reglas);
    }

    @GetMapping("/reglas/{especialidad}")
    public ResponseEntity<List<ReasignacionRegla>> getReglasPorEspecialidad(@PathVariable String especialidad) {
        List<ReasignacionRegla> reglas = reglaRepository.findByEspecialidadAndActiva(especialidad, true);
        return ResponseEntity.ok(reglas);
    }

    @PostMapping("/reglas")
    public ResponseEntity<ReasignacionRegla> crearRegla(@RequestBody ReasignacionRegla regla) {
        ReasignacionRegla nuevaRegla = reglaRepository.save(regla);
        return ResponseEntity.ok(nuevaRegla);
    }

    @PutMapping("/reglas/{id}")
    public ResponseEntity<ReasignacionRegla> actualizarRegla(@PathVariable Long id, @RequestBody ReasignacionRegla reglaActualizada) {
        Optional<ReasignacionRegla> optionalRegla = reglaRepository.findById(id);
        if (optionalRegla.isPresent()) {
            ReasignacionRegla regla = optionalRegla.get();
            regla.setEspecialidad(reglaActualizada.getEspecialidad());
            regla.setReglaTipo(reglaActualizada.getReglaTipo());
            regla.setValor(reglaActualizada.getValor());
            regla.setActiva(reglaActualizada.isActiva());
            regla.setDescripcion(reglaActualizada.getDescripcion());

            ReasignacionRegla updatedRegla = reglaRepository.save(regla);
            return ResponseEntity.ok(updatedRegla);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/reglas/{id}")
    public ResponseEntity<Void> eliminarRegla(@PathVariable Long id) {
        if (reglaRepository.existsById(id)) {
            reglaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/inicializar")
    public ResponseEntity<String> inicializarReglas() {
        reasignacionService.inicializarReglasPorDefecto();
        return ResponseEntity.ok("Reglas de reasignación inicializadas correctamente");
    }

    @PostMapping("/ejecutar")
    public ResponseEntity<String> ejecutarReasignacionManual() {
        reasignacionService.ejecutarReasignacionAutomatica();
        return ResponseEntity.ok("Reasignación ejecutada manualmente");
    }
}
