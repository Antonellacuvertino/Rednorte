package cl.duoc.rednorte.controller;

import cl.duoc.rednorte.dto.PacienteDTO;
import cl.duoc.rednorte.dto.CitaDTO;
import cl.duoc.rednorte.feign.PacienteClient;
import cl.duoc.rednorte.feign.CitaClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@RestController
@RequestMapping("/bff")
@CrossOrigin(origins = "*") // Permite peticiones desde React
public class PacienteCitaController {

    @Autowired
    private PacienteClient pacienteClient;

    @Autowired
    private CitaClient citaClient;

    @GetMapping("/pacientes")
    public List<PacienteDTO> getPacientes() {
        return pacienteClient.getAllPacientes();
    }

    @PostMapping("/pacientes")
    public ResponseEntity<PacienteDTO> crearPaciente(@RequestBody PacienteDTO paciente) {
        PacienteDTO nuevoPaciente = pacienteClient.createPaciente(paciente);
        return ResponseEntity.ok(nuevoPaciente);
    }

    @GetMapping("/paciente-citas/{id}")
    public ResponseEntity<Map<String, Object>> getPacienteConCitas(@PathVariable Long id) {
        PacienteDTO paciente = pacienteClient.getPacienteById(id);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }

        List<CitaDTO> citas = citaClient.getCitasByPacienteId(id);

        Map<String, Object> response = new HashMap<>();
        response.put("paciente", paciente);
        response.put("citas", citas);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/citas")
    public List<CitaDTO> getAllCitas() {
        return citaClient.getAllCitas();
    }
}
