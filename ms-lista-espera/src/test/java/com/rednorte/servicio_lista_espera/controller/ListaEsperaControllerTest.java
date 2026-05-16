package com.rednorte.servicio_lista_espera.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rednorte.servicio_lista_espera.entity.ListaEspera;
import com.rednorte.servicio_lista_espera.repository.ListaEsperaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ListaEsperaController.class)
class ListaEsperaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ListaEsperaRepository listaEsperaRepository;

    @Test
    void getPendientesRetornaListaOrdenada() throws Exception {
        when(listaEsperaRepository.findPendientesOrdenadasPorPrioridad())
                .thenReturn(List.of(new ListaEspera(1L, "CARDIOLOGIA", "ALTA")));

        mockMvc.perform(get("/api/lista-espera/pendientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pacienteId").value(1))
                .andExpect(jsonPath("$[0].especialidad").value("CARDIOLOGIA"));
    }

    @Test
    void crearListaEsperaGuardaRegistroPendiente() throws Exception {
        ListaEspera registro = new ListaEspera(2L, "PEDIATRIA", "MEDIA");
        registro.setId(10L);

        when(listaEsperaRepository.save(any(ListaEspera.class))).thenReturn(registro);

        mockMvc.perform(post("/api/lista-espera")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void atenderListaEsperaMarcaComoAtendido() throws Exception {
        ListaEspera registro = new ListaEspera(3L, "TRAUMATOLOGIA", "BAJA");

        when(listaEsperaRepository.findById(1L)).thenReturn(Optional.of(registro));
        when(listaEsperaRepository.save(any(ListaEspera.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/lista-espera/1/atender"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ATENDIDO"));
    }
}
