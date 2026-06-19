package com.rednorte.servicio_lista_espera.controller;

import java.util.List;

import com.rednorte.servicio_lista_espera.entity.ListaEspera;
import com.rednorte.servicio_lista_espera.service.ListaEsperaService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListaEsperaControllerTest {

    @Test
    void delegaTodasLasOperacionesEnElServicio() {
        ListaEsperaService service = mock(ListaEsperaService.class);
        ListaEsperaController controller = new ListaEsperaController(service);
        ListaEspera registro = new ListaEspera(1L, "CARDIOLOGIA", "ALTA");

        when(service.listar()).thenReturn(List.of(registro));
        when(service.listarPendientes()).thenReturn(List.of(registro));
        when(service.listarPendientesPorEspecialidad("CARDIOLOGIA")).thenReturn(List.of(registro));
        when(service.listarPorPaciente(1L)).thenReturn(List.of(registro));
        when(service.crear(registro)).thenReturn(registro);
        when(service.atender(1L)).thenReturn(registro);
        when(service.cancelar(1L)).thenReturn(registro);

        assertEquals(1, controller.getAllListaEspera().getBody().size());
        assertEquals(1, controller.getListaEsperaPendiente().getBody().size());
        assertEquals(1, controller.getListaEsperaPendientePorEspecialidad("CARDIOLOGIA").getBody().size());
        assertEquals(1, controller.getListaEsperaPorPaciente(1L).getBody().size());
        assertEquals(201, controller.crearListaEspera(registro).getStatusCode().value());
        assertEquals(registro, controller.atenderListaEspera(1L).getBody());
        assertEquals(registro, controller.cancelarListaEspera(1L).getBody());
        assertEquals(204, controller.eliminarListaEspera(1L).getStatusCode().value());
    }
}
