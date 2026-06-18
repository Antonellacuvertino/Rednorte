package com.rednorte.servicio_pacientes.service;

import com.rednorte.servicio_pacientes.model.Paciente;
import com.rednorte.servicio_pacientes.repository.PacienteRepository;
import com.rednorte.servicio_pacientes.service.impl.PacienteServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PacienteServiceImplTest {

    @Test
    void delegaOperacionesCrudAlRepositorio() {
        PacienteRepository repository = mock(PacienteRepository.class);
        PacienteServiceImpl service = new PacienteServiceImpl(repository);
        Paciente paciente = new Paciente();

        when(repository.findAll()).thenReturn(List.of(paciente));
        when(repository.findById(1L)).thenReturn(Optional.of(paciente));
        when(repository.findByRut("11111111-1")).thenReturn(paciente);
        when(repository.save(paciente)).thenReturn(paciente);

        assertEquals(1, service.obtenerTodos().size());
        assertEquals(paciente, service.obtenerPorId(1L).orElseThrow());
        assertEquals(paciente, service.obtenerPorRut("11111111-1"));
        assertEquals(paciente, service.guardar(paciente));

        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }
}
