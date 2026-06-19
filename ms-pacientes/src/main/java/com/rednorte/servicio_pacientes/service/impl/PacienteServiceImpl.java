package com.rednorte.servicio_pacientes.service.impl;

import com.rednorte.servicio_pacientes.model.Paciente;
import com.rednorte.servicio_pacientes.repository.PacienteRepository;
import com.rednorte.servicio_pacientes.service.PacienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Implementa la persistencia transaccional de pacientes mediante JPA.
 */
@Service
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository repository;

    public PacienteServiceImpl(PacienteRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paciente> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Paciente obtenerPorRut(String rut) {
        return repository.findByRut(rut);
    }

    @Override
    @Transactional
    public Paciente guardar(Paciente paciente) {
        return repository.save(paciente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
