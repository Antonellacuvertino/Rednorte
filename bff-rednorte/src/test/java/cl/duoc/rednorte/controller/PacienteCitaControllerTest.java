package cl.duoc.rednorte.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cl.duoc.rednorte.dto.CitaDTO;
import cl.duoc.rednorte.dto.PacienteDTO;
import cl.duoc.rednorte.feign.CitaClient;
import cl.duoc.rednorte.feign.ListaEsperaClient;
import cl.duoc.rednorte.feign.ReasignacionClient;
import cl.duoc.rednorte.feign.PacienteClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

@WebMvcTest(PacienteCitaController.class)
class PacienteCitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteClient pacienteClient;

    @MockBean
    private CitaClient citaClient;

    @MockBean
    private ListaEsperaClient listaEsperaClient;

    @MockBean
    private ReasignacionClient reasignacionClient;

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
}
