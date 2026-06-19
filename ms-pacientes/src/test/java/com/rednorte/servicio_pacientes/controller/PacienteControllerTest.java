package com.rednorte.servicio_pacientes.controller;

import com.rednorte.servicio_pacientes.model.Paciente;
import com.rednorte.servicio_pacientes.exception.ResourceNotFoundException;
import com.rednorte.servicio_pacientes.service.PacienteService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PacienteControllerTest {

    @Test
    void cubreListadoCreacionYBusquedas() {
        PacienteService service = mock(PacienteService.class);
        PacienteController controller = new PacienteController(service);
        Paciente paciente = new Paciente();

        when(service.obtenerTodos()).thenReturn(List.of(paciente));
        when(service.guardar(paciente)).thenReturn(paciente);
        when(service.obtenerPorId(1L)).thenReturn(Optional.of(paciente));
        when(service.obtenerPorId(99L)).thenReturn(Optional.empty());
        when(service.obtenerPorRut("11111111-1")).thenReturn(paciente);
        when(service.obtenerPorRut("00000000-0")).thenReturn(null);

        assertEquals(1, controller.listar().getBody().size());
        assertEquals(201, controller.crear(paciente).getStatusCode().value());
        assertEquals(200, controller.buscarPorId(1L).getStatusCode().value());
        assertThrows(ResourceNotFoundException.class, () -> controller.buscarPorId(99L));
        assertEquals(200, controller.buscarPorRut("11111111-1").getStatusCode().value());
        assertThrows(ResourceNotFoundException.class, () -> controller.buscarPorRut("00000000-0"));
    }
}
