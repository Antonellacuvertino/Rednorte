package cl.duoc.rednorte.controller;

import cl.duoc.rednorte.dto.PacienteDTO;
import cl.duoc.rednorte.dto.CitaDTO;
import cl.duoc.rednorte.dto.ListaEsperaDTO;
import cl.duoc.rednorte.dto.ReasignacionDTO;
import cl.duoc.rednorte.dto.ReasignacionRequestDTO;
import cl.duoc.rednorte.dto.AuditEventDTO;
import cl.duoc.rednorte.dto.NotificationDTO;
import cl.duoc.rednorte.feign.AuditClient;
import cl.duoc.rednorte.feign.PacienteClient;
import cl.duoc.rednorte.feign.CitaClient;
import cl.duoc.rednorte.feign.ListaEsperaClient;
import cl.duoc.rednorte.feign.ReasignacionClient;
import cl.duoc.rednorte.feign.NotificationClient;
import cl.duoc.rednorte.messaging.AuditEventPublisher;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

/**
 * Fachada REST del frontend para los seis microservicios de RedNorte.
 */
@RestController
@RequestMapping("/bff")
@CrossOrigin(origins = "*") // Permite peticiones desde React
@Tag(name = "BFF RedNorte", description = "Operaciones privadas consumidas por React")
public class PacienteCitaController {

    @Autowired
    private PacienteClient pacienteClient;

    @Autowired
    private CitaClient citaClient;

    @Autowired
    private ListaEsperaClient listaEsperaClient;

    @Autowired
    private ReasignacionClient reasignacionClient;

    @Autowired
    private AuditEventPublisher auditEventPublisher;

    @Autowired
    private AuditClient auditClient;

    @Autowired
    private NotificationClient notificationClient;

    @GetMapping("/pacientes")
    @Cacheable("pacientes")
    public List<PacienteDTO> getPacientes() {
        return pacienteClient.getAllPacientes();
    }

    @PostMapping("/pacientes")
    @CacheEvict(value = "pacientes", allEntries = true)
    public ResponseEntity<PacienteDTO> crearPaciente(@RequestBody PacienteDTO paciente) {
        PacienteDTO nuevoPaciente = pacienteClient.createPaciente(paciente);
        auditEventPublisher.publish("PACIENTE_CREADO", nuevoPaciente);
        return ResponseEntity.ok(nuevoPaciente);
    }

    @GetMapping("/paciente-citas/{id}")
    @Cacheable(value = "paciente-citas", key = "#id")
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
        CitaDTO nuevaCita = citaClient.createCita(tipo, cita);
        auditEventPublisher.publish("CITA_CREADA", nuevaCita);
        return nuevaCita;
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

    @GetMapping("/reasignaciones")
    public List<ReasignacionDTO> getReasignaciones() {
        return reasignacionClient.getAll();
    }

    @PostMapping("/reasignaciones")
    public ReasignacionDTO reprogramarCita(@RequestBody ReasignacionRequestDTO request) {
        ReasignacionDTO result = reasignacionClient.reprogramar(request);
        auditEventPublisher.publish("CITA_REPROGRAMADA", result);
        return result;
    }

    @GetMapping("/auditoria")
    public List<AuditEventDTO> getAuditoria(@RequestParam(required = false) String tipo) {
        return auditClient.findAll(tipo);
    }

    @GetMapping("/notificaciones")
    public List<NotificationDTO> getNotificaciones(
            @RequestParam(defaultValue = "false") boolean noLeidas) {
        return notificationClient.findAll(noLeidas);
    }

    @PutMapping("/notificaciones/{id}/leer")
    public NotificationDTO leerNotificacion(@PathVariable Long id) {
        return notificationClient.markAsRead(id);
    }
}
