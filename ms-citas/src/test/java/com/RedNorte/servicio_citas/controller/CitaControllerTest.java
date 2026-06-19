package com.RedNorte.servicio_citas.controller;

import java.util.List;

import com.RedNorte.servicio_citas.model.Cita;
import com.RedNorte.servicio_citas.service.CitaService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CitaControllerTest {

    @Test
    void delegaAgendaConsultasYReprogramacion() {
        CitaService service = mock(CitaService.class);
        CitaController controller = new CitaController(service);
        Cita cita = Cita.builder().id(10L).pacienteId(1L).fecha("2026-07-01").hora("10:00").build();

        when(service.listar()).thenReturn(List.of(cita));
        when(service.listarPorPaciente(1L)).thenReturn(List.of(cita));
        when(service.buscarPorId(10L)).thenReturn(cita);
        when(service.agendar("URGENCIA", cita)).thenReturn(cita);
        when(service.reprogramar(10L, cita)).thenReturn(cita);

        assertEquals(1, controller.listar().size());
        assertEquals(1, controller.listarPorPaciente(1L).size());
        assertEquals(cita, controller.buscarPorId(10L).getBody());
        assertEquals(201, controller.agendar("URGENCIA", cita).getStatusCode().value());
        assertEquals(cita, controller.reprogramar(10L, cita).getBody());
    }
}
