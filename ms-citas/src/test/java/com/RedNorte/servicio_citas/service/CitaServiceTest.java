package com.RedNorte.servicio_citas.service;

import java.util.List;
import java.util.Optional;

import com.RedNorte.servicio_citas.exception.ResourceNotFoundException;
import com.RedNorte.servicio_citas.factory.CitaFactory;
import com.RedNorte.servicio_citas.model.Cita;
import com.RedNorte.servicio_citas.repository.CitaRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CitaServiceTest {

    @Test
    void listaAgendaBuscaYReprograma() {
        CitaRepository repository = mock(CitaRepository.class);
        CitaFactory factory = mock(CitaFactory.class);
        CitaService service = new CitaService(repository, factory);
        Cita datos = Cita.builder().pacienteId(1L).fecha("2026-07-01").hora("10:00").build();
        Cita cita = Cita.builder().id(7L).pacienteId(1L).fecha("2026-07-01").hora("10:00").build();

        when(repository.findAll()).thenReturn(List.of(cita));
        when(repository.findByPacienteId(1L)).thenReturn(List.of(cita));
        when(repository.findById(7L)).thenReturn(Optional.of(cita));
        when(factory.crearCita("URGENCIA", 1L, null, "2026-07-01", "10:00")).thenReturn(cita);
        when(repository.save(any(Cita.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertEquals(1, service.listar().size());
        assertEquals(1, service.listarPorPaciente(1L).size());
        assertEquals(cita, service.buscarPorId(7L));
        assertEquals(cita, service.agendar("URGENCIA", datos));

        Cita cambios = Cita.builder().fecha("2026-07-02").hora("11:30").build();
        assertEquals("11:30", service.reprogramar(7L, cambios).getHora());
    }

    @Test
    void rechazaDatosInvalidosYRecursosInexistentes() {
        CitaRepository repository = mock(CitaRepository.class);
        CitaService service = new CitaService(repository, mock(CitaFactory.class));
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(99L));
        assertThrows(IllegalArgumentException.class, () -> service.agendar("", new Cita()));
        assertThrows(IllegalArgumentException.class, () -> service.agendar("GENERAL", null));
        assertThrows(IllegalArgumentException.class, () -> service.reprogramar(1L, null));
        assertThrows(IllegalArgumentException.class,
                () -> service.reprogramar(1L, Cita.builder().fecha("2026-07-01").build()));
    }
}
