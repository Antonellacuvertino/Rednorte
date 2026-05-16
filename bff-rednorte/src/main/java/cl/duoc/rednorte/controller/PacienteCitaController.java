package cl.duoc.rednorte.controller;

import cl.duoc.rednorte.dto.PacienteDTO;
import cl.duoc.rednorte.dto.CitaDTO;
import cl.duoc.rednorte.dto.ListaEsperaDTO;
import cl.duoc.rednorte.dto.ReasignacionReglaDTO;
import cl.duoc.rednorte.feign.PacienteClient;
import cl.duoc.rednorte.feign.CitaClient;
import cl.duoc.rednorte.feign.ListaEsperaClient;
import cl.duoc.rednorte.feign.ReasignacionClient;
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

    @Autowired
    private ListaEsperaClient listaEsperaClient;

    @Autowired
    private ReasignacionClient reasignacionClient;

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

    @PostMapping("/citas")
    public CitaDTO crearCita(@RequestBody CitaDTO cita) {
        String tipo = cita.getTipoCita() == null || cita.getTipoCita().isBlank() ? "GENERAL" : cita.getTipoCita();
        return citaClient.createCita(tipo, cita);
    }

    @GetMapping("/lista-espera")
    public List<ListaEsperaDTO> getListaEspera() {
        return listaEsperaClient.getAll();
    }

    @GetMapping("/lista-espera/pendientes")
    public List<ListaEsperaDTO> getListaEsperaPendiente() {
        return listaEsperaClient.getPendientes();
    }

    @PostMapping("/lista-espera")
    public ListaEsperaDTO crearListaEspera(@RequestBody ListaEsperaDTO listaEspera) {
        return listaEsperaClient.create(listaEspera);
    }

    @PutMapping("/lista-espera/{id}/atender")
    public ListaEsperaDTO atenderListaEspera(@PathVariable Long id) {
        return listaEsperaClient.atender(id);
    }

    @PutMapping("/lista-espera/{id}/cancelar")
    public ListaEsperaDTO cancelarListaEspera(@PathVariable Long id) {
        return listaEsperaClient.cancelar(id);
    }

    @GetMapping("/reasignacion/reglas")
    public List<ReasignacionReglaDTO> getReglasReasignacion() {
        return reasignacionClient.getReglas();
    }

    @PostMapping("/reasignacion/reglas")
    public ReasignacionReglaDTO crearReglaReasignacion(@RequestBody ReasignacionReglaDTO regla) {
        return reasignacionClient.createRegla(regla);
    }

    @PostMapping("/reasignacion/inicializar")
    public String inicializarReasignacion() {
        return reasignacionClient.inicializar();
    }

    @PostMapping("/reasignacion/ejecutar")
    public String ejecutarReasignacion() {
        return reasignacionClient.ejecutar();
    }
}
