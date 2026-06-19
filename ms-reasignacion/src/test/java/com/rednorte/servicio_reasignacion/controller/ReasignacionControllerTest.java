package com.rednorte.servicio_reasignacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rednorte.servicio_reasignacion.entity.Reasignacion;
import com.rednorte.servicio_reasignacion.service.ReasignacionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReasignacionController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReasignacionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReasignacionService service;

    @Test
    void listaYRegistraReasignaciones() throws Exception {
        Reasignacion result = new Reasignacion();
        result.setId(1L);
        result.setCitaId(5L);
        result.setFechaNueva("2026-07-10");
        result.setHoraNueva("15:30");
        when(service.findAll()).thenReturn(List.of(result));
        when(service.reprogramar(any())).thenReturn(result);

        mockMvc.perform(get("/api/reasignaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].citaId").value(5));

        mockMvc.perform(post("/api/reasignaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Request(
                                5L, "2026-07-10", "15:30", "Paciente atrasado", "Dra. Norte"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fechaNueva").value("2026-07-10"));
    }

    @Test
    void retornaBadRequestCuandoNoPuedeReprogramar() throws Exception {
        when(service.reprogramar(any())).thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/api/reasignaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    private record Request(Long citaId, String fechaNueva, String horaNueva, String motivo,
                           String medicoResponsable) {
    }
}
