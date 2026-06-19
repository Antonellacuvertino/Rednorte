package com.RedNorte.servicio_citas.service;

import java.util.List;

import com.RedNorte.servicio_citas.exception.ResourceNotFoundException;
import com.RedNorte.servicio_citas.factory.CitaFactory;
import com.RedNorte.servicio_citas.model.Cita;
import com.RedNorte.servicio_citas.repository.CitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Centraliza las reglas de agenda, prioridad y reprogramacion de citas.
 */
@Service
public class CitaService {

    private final CitaRepository repository;
    private final CitaFactory citaFactory;

    public CitaService(CitaRepository repository, CitaFactory citaFactory) {
        this.repository = repository;
        this.citaFactory = citaFactory;
    }

    /** @return todas las citas registradas */
    @Transactional(readOnly = true)
    public List<Cita> listar() {
        return repository.findAll();
    }

    /**
     * @param pacienteId identificador del paciente
     * @return citas asociadas al paciente
     */
    @Transactional(readOnly = true)
    public List<Cita> listarPorPaciente(Long pacienteId) {
        return repository.findByPacienteId(pacienteId);
    }

    /**
     * @param id identificador de la cita
     * @return cita encontrada
     * @throws ResourceNotFoundException si la cita no existe
     */
    @Transactional(readOnly = true)
    public Cita buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada: " + id));
    }

    /**
     * @param tipo tipo usado para calcular la prioridad
     * @param datos datos de agenda
     * @return cita persistida
     */
    @Transactional
    public Cita agendar(String tipo, Cita datos) {
        if (datos == null || tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo y los datos de la cita son obligatorios");
        }
        Cita cita = citaFactory.crearCita(
                tipo, datos.getPacienteId(), datos.getEspecialidad(), datos.getFecha(), datos.getHora());
        return repository.save(cita);
    }

    /**
     * @param id identificador de la cita
     * @param cambios nueva fecha y hora
     * @return cita actualizada
     */
    @Transactional
    public Cita reprogramar(Long id, Cita cambios) {
        if (cambios == null || cambios.getFecha() == null || cambios.getHora() == null) {
            throw new IllegalArgumentException("La fecha y hora son obligatorias");
        }
        Cita cita = buscarPorId(id);
        cita.setFecha(cambios.getFecha());
        cita.setHora(cambios.getHora());
        return repository.save(cita);
    }
}
