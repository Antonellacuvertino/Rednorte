package com.RedNorte.servicio_citas.controller;

import com.RedNorte.servicio_citas.factory.CitaFactory;
import com.RedNorte.servicio_citas.model.Cita;
import com.RedNorte.servicio_citas.repository.CitaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CitaControllerTest {

    @Test
    void listaFiltraYAgendaCitas() {
        CitaRepository repository = mock(CitaRepository.class);
        CitaFactory factory = mock(CitaFactory.class);
        CitaController controller = new CitaController();
        ReflectionTestUtils.setField(controller, "repository", repository);
        ReflectionTestUtils.setField(controller, "citaFactory", factory);

        Cita datos = Cita.builder()
                .pacienteId(1L)
                .especialidad("CARDIOLOGIA")
                .fecha("2026-06-20")
                .hora("10:00")
                .build();
        Cita creada = Cita.builder().id(10L).pacienteId(1L).build();

        when(repository.findAll()).thenReturn(List.of(creada));
        when(repository.findByPacienteId(1L)).thenReturn(List.of(creada));
        when(factory.crearCita("URGENCIA", 1L, "CARDIOLOGIA", "2026-06-20", "10:00"))
                .thenReturn(creada);
        when(repository.save(any(Cita.class))).thenReturn(creada);
        when(repository.findById(10L)).thenReturn(Optional.of(creada));

        assertEquals(1, controller.listar().size());
        assertEquals(1, controller.listarPorPaciente(1L).size());
        assertEquals(creada, controller.agendar("URGENCIA", datos));
        assertEquals(200, controller.buscarPorId(10L).getStatusCode().value());
        assertEquals(200, controller.reprogramar(10L, datos).getStatusCode().value());
        verify(repository, atLeastOnce()).save(creada);
    }
}
