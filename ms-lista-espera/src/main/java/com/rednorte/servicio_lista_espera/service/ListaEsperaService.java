package com.rednorte.servicio_lista_espera.service;

import java.time.LocalDateTime;
import java.util.List;

import com.rednorte.servicio_lista_espera.entity.ListaEspera;
import com.rednorte.servicio_lista_espera.exception.ResourceNotFoundException;
import com.rednorte.servicio_lista_espera.repository.ListaEsperaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Aplica las transiciones de estado y consultas de la lista de espera.
 */
@Service
public class ListaEsperaService {

    private final ListaEsperaRepository repository;

    public ListaEsperaService(ListaEsperaRepository repository) {
        this.repository = repository;
    }

    /** @return todos los registros */
    @Transactional(readOnly = true)
    public List<ListaEspera> listar() {
        return repository.findAll();
    }

    /** @return registros pendientes ordenados por prioridad */
    @Transactional(readOnly = true)
    public List<ListaEspera> listarPendientes() {
        return repository.findPendientesOrdenadasPorPrioridad();
    }

    /**
     * @param especialidad especialidad clinica
     * @return registros pendientes filtrados
     */
    @Transactional(readOnly = true)
    public List<ListaEspera> listarPendientesPorEspecialidad(String especialidad) {
        return repository.findPendientesPorEspecialidadOrdenadasPorPrioridad(especialidad.toUpperCase());
    }

    /**
     * @param pacienteId identificador del paciente
     * @return registros del paciente
     */
    @Transactional(readOnly = true)
    public List<ListaEspera> listarPorPaciente(Long pacienteId) {
        return repository.findByPacienteId(pacienteId);
    }

    /**
     * @param registro datos de espera
     * @return registro persistido
     */
    @Transactional
    public ListaEspera crear(ListaEspera registro) {
        if (registro == null || registro.getPacienteId() == null) {
            throw new IllegalArgumentException("El paciente es obligatorio");
        }
        return repository.save(registro);
    }

    /**
     * @param id identificador del registro
     * @return registro atendido
     */
    @Transactional
    public ListaEspera atender(Long id) {
        ListaEspera registro = buscar(id);
        registro.setEstado("ATENDIDO");
        registro.setFechaAtencion(LocalDateTime.now());
        return repository.save(registro);
    }

    /**
     * @param id identificador del registro
     * @return registro cancelado
     */
    @Transactional
    public ListaEspera cancelar(Long id) {
        ListaEspera registro = buscar(id);
        registro.setEstado("CANCELADO");
        return repository.save(registro);
    }

    /**
     * @param id identificador del registro
     */
    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Registro de espera no encontrado: " + id);
        }
        repository.deleteById(id);
    }

    private ListaEspera buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de espera no encontrado: " + id));
    }
}
