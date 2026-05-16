package com.rednorte.servicio_reasignacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rednorte.servicio_reasignacion.entity.ReasignacionRegla;
import com.rednorte.servicio_reasignacion.repository.ReasignacionReglaRepository;
import com.rednorte.servicio_reasignacion.service.ReasignacionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReasignacionController.class)
class ReasignacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReasignacionReglaRepository reglaRepository;

    @MockBean
    private ReasignacionService reasignacionService;

    @Test
    void getReglasActivasRetornaReglas() throws Exception {
        when(reglaRepository.findByActiva(true))
                .thenReturn(List.of(new ReasignacionRegla("CARDIOLOGIA", "TIEMPO_ESPERA", "30", "Regla")));

        mockMvc.perform(get("/api/reasignacion/reglas/activas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].especialidad").value("CARDIOLOGIA"))
                .andExpect(jsonPath("$[0].activa").value(true));
    }

    @Test
    void crearReglaGuardaRegla() throws Exception {
        ReasignacionRegla regla = new ReasignacionRegla("PEDIATRIA", "CAPACIDAD_MAXIMA", "10", "Capacidad");
        regla.setId(5L);

        when(reglaRepository.save(any(ReasignacionRegla.class))).thenReturn(regla);

        mockMvc.perform(post("/api/reasignacion/reglas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regla)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.reglaTipo").value("CAPACIDAD_MAXIMA"));
    }

    @Test
    void inicializarReglasRespondeOk() throws Exception {
        doNothing().when(reasignacionService).inicializarReglasPorDefecto();

        mockMvc.perform(post("/api/reasignacion/inicializar"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("inicializadas correctamente")));
    }
}
