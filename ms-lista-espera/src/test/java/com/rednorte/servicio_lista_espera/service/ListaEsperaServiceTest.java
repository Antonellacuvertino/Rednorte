package com.rednorte.servicio_lista_espera.service;

import java.util.List;
import java.util.Optional;

import com.rednorte.servicio_lista_espera.entity.ListaEspera;
import com.rednorte.servicio_lista_espera.exception.ResourceNotFoundException;
import com.rednorte.servicio_lista_espera.repository.ListaEsperaRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListaEsperaServiceTest {

    @Test
    void consultaCreaAtiendeCancelaYElimina() {
        ListaEsperaRepository repository = mock(ListaEsperaRepository.class);
        ListaEsperaService service = new ListaEsperaService(repository);
        ListaEspera registro = new ListaEspera(1L, "CARDIOLOGIA", "ALTA");

        when(repository.findAll()).thenReturn(List.of(registro));
        when(repository.findPendientesOrdenadasPorPrioridad()).thenReturn(List.of(registro));
        when(repository.findPendientesPorEspecialidadOrdenadasPorPrioridad("CARDIOLOGIA"))
                .thenReturn(List.of(registro));
        when(repository.findByPacienteId(1L)).thenReturn(List.of(registro));
        when(repository.findById(1L)).thenReturn(Optional.of(registro));
        when(repository.save(any(ListaEspera.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(repository.existsById(1L)).thenReturn(true);

        assertEquals(1, service.listar().size());
        assertEquals(1, service.listarPendientes().size());
        assertEquals(1, service.listarPendientesPorEspecialidad("cardiologia").size());
        assertEquals(1, service.listarPorPaciente(1L).size());
        assertEquals(registro, service.crear(registro));
        assertEquals("ATENDIDO", service.atender(1L).getEstado());
        assertNotNull(registro.getFechaAtencion());
        assertEquals("CANCELADO", service.cancelar(1L).getEstado());
        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void rechazaRegistrosInvalidosOInexistentes() {
        ListaEsperaRepository repository = mock(ListaEsperaRepository.class);
        ListaEsperaService service = new ListaEsperaService(repository);
        when(repository.findById(99L)).thenReturn(Optional.empty());
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.crear(null));
        assertThrows(IllegalArgumentException.class, () -> service.crear(new ListaEspera()));
        assertThrows(ResourceNotFoundException.class, () -> service.atender(99L));
        assertThrows(ResourceNotFoundException.class, () -> service.cancelar(99L));
        assertThrows(ResourceNotFoundException.class, () -> service.eliminar(99L));
    }
}
