package cl.duoc.rednorte.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cl.duoc.rednorte.dto.CitaDTO;
import cl.duoc.rednorte.dto.PacienteDTO;
import cl.duoc.rednorte.dto.ListaEsperaDTO;
import cl.duoc.rednorte.dto.ReasignacionDTO;
import cl.duoc.rednorte.dto.ReasignacionRequestDTO;
import cl.duoc.rednorte.dto.AuditEventDTO;
import cl.duoc.rednorte.dto.NotificationDTO;
import cl.duoc.rednorte.feign.CitaClient;
import cl.duoc.rednorte.feign.AuditClient;
import cl.duoc.rednorte.feign.ListaEsperaClient;
import cl.duoc.rednorte.feign.ReasignacionClient;
import cl.duoc.rednorte.feign.PacienteClient;
import cl.duoc.rednorte.feign.NotificationClient;
import cl.duoc.rednorte.messaging.AuditEventPublisher;
import cl.duoc.rednorte.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.time.Instant;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(PacienteCitaController.class)
@AutoConfigureMockMvc(addFilters = false)
class PacienteCitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PacienteCitaController controller;

    @MockBean
    private PacienteClient pacienteClient;

    @MockBean
    private CitaClient citaClient;

    @MockBean
    private ListaEsperaClient listaEsperaClient;

    @MockBean
    private ReasignacionClient reasignacionClient;

    @MockBean
    private AuditEventPublisher auditEventPublisher;

    @MockBean
    private AuditClient auditClient;

    @MockBean
    private NotificationClient notificationClient;

    @MockBean
    private JwtService jwtService;

    @Test
    void testGetPacientes() throws Exception {
        when(pacienteClient.getAllPacientes())
                .thenReturn(List.of(new PacienteDTO(1L, "Juan", "Perez", "12345678-9", "Historial limpio")));

        mockMvc.perform(get("/bff/pacientes")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    void testGetPacienteCitas() throws Exception {
        PacienteDTO paciente = new PacienteDTO(1L, "Juan", "Perez", "12345678-9", "Historial limpio");
        CitaDTO cita = new CitaDTO(1L, "2026-05-10", "10:00", 1L);

        when(pacienteClient.getPacienteById(1L)).thenReturn(paciente);
        when(citaClient.getCitasByPacienteId(1L)).thenReturn(Collections.singletonList(cita));

        mockMvc.perform(get("/bff/paciente-citas/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paciente.nombre").value("Juan"))
                .andExpect(jsonPath("$.citas[0].fecha").value("2026-05-10"))
                .andExpect(jsonPath("$.citas[0].hora").value("10:00"));
    }

    @Test
    void testGetPacienteCitasNotFound() throws Exception {
        when(pacienteClient.getPacienteById(1L)).thenReturn(null);

        mockMvc.perform(get("/bff/paciente-citas/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCrearPaciente() throws Exception {
        PacienteDTO paciente = new PacienteDTO(4L, "Sofia", "Munoz", "44555666-7", "Ingreso inicial");

        when(pacienteClient.createPaciente(any(PacienteDTO.class))).thenReturn(paciente);

        mockMvc.perform(post("/bff/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "rut": "44555666-7",
                          "nombre": "Sofia",
                          "apellido": "Munoz",
                          "historialClinico": "Ingreso inicial"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sofia"))
                .andExpect(jsonPath("$.rut").value("44555666-7"));
    }

    @Test
    void cubreCitasListaEsperaReasignacionAuditoriaYNotificaciones() {
        CitaDTO cita = new CitaDTO(1L, "2026-07-01", "10:00", 4L);
        ListaEsperaDTO espera = new ListaEsperaDTO();
        espera.setId(2L);
        ReasignacionDTO reasignacion = new ReasignacionDTO(
                3L, 1L, 4L, "2026-07-01", "10:00", "2026-07-01", "11:00",
                "Atraso", "Dra. Norte", "COMPLETADA", LocalDateTime.now());
        ReasignacionRequestDTO request = new ReasignacionRequestDTO(
                1L, "2026-07-01", "11:00", "Atraso", "Dra. Norte");
        AuditEventDTO audit = new AuditEventDTO(1L, "CITA_CREADA", Instant.now(), Instant.now(), "{}");
        NotificationDTO notification = new NotificationDTO(
                1L, "CITA_CREADA", "Nueva cita", "Mensaje", false, Instant.now());

        when(citaClient.getAllCitas()).thenReturn(List.of(cita));
        when(citaClient.createCita(eq("GENERAL"), any(CitaDTO.class))).thenReturn(cita);
        when(listaEsperaClient.getAll()).thenReturn(List.of(espera));
        when(listaEsperaClient.getPendientes()).thenReturn(List.of(espera));
        when(listaEsperaClient.create(espera)).thenReturn(espera);
        when(listaEsperaClient.atender(2L)).thenReturn(espera);
        when(listaEsperaClient.cancelar(2L)).thenReturn(espera);
        when(reasignacionClient.getAll()).thenReturn(List.of(reasignacion));
        when(reasignacionClient.reprogramar(request)).thenReturn(reasignacion);
        when(auditClient.findAll("CITA_CREADA")).thenReturn(List.of(audit));
        when(notificationClient.findAll(true)).thenReturn(List.of(notification));
        when(notificationClient.markAsRead(1L)).thenReturn(notification);

        assertEquals(1, controller.getAllCitas().size());
        assertEquals(cita, controller.crearCita(cita));
        assertEquals(1, controller.getListaEspera().size());
        assertEquals(1, controller.getListaEsperaPendiente().size());
        assertEquals(espera, controller.crearListaEspera(espera));
        assertEquals(espera, controller.atenderListaEspera(2L));
        assertEquals(espera, controller.cancelarListaEspera(2L));
        assertEquals(1, controller.getReasignaciones().size());
        assertEquals(reasignacion, controller.reprogramarCita(request));
        assertEquals(1, controller.getAuditoria("CITA_CREADA").size());
        assertEquals(1, controller.getNotificaciones(true).size());
        assertEquals(notification, controller.leerNotificacion(1L));
    }
}
