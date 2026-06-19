package com.rednorte.servicio_pacientes.service;

import java.util.List;
import java.util.Optional;

import com.rednorte.servicio_pacientes.model.Paciente;

/**
 * Define las operaciones de negocio disponibles para pacientes.
 */
public interface PacienteService {

    /**
     * Lista todos los pacientes.
     *
     * @return pacientes registrados
     */
    List<Paciente> obtenerTodos();

    /**
     * Busca un paciente por id.
     *
     * @param id identificador interno
     * @return paciente, si existe
     */
    Optional<Paciente> obtenerPorId(Long id);

    /**
     * Busca un paciente por RUT.
     *
     * @param rut identificador nacional
     * @return paciente o {@code null} cuando no existe
     */
    Paciente obtenerPorRut(String rut);

    /**
     * Guarda un paciente nuevo o modificado.
     *
     * @param paciente datos del paciente
     * @return entidad persistida
     */
    Paciente guardar(Paciente paciente);

    /**
     * Elimina un paciente por id.
     *
     * @param id identificador interno
     */
    void eliminar(Long id);
}
