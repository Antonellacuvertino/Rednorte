package com.rednorte.servicio_lista_espera.repository;

import com.rednorte.servicio_lista_espera.entity.ListaEspera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListaEsperaRepository extends JpaRepository<ListaEspera, Long> {

    List<ListaEspera> findByEstado(String estado);

    List<ListaEspera> findByEspecialidadAndEstado(String especialidad, String estado);

    @Query("SELECT le FROM ListaEspera le WHERE le.estado = 'PENDIENTE' ORDER BY " +
           "CASE le.prioridad WHEN 'ALTA' THEN 1 WHEN 'MEDIA' THEN 2 WHEN 'BAJA' THEN 3 END, " +
           "le.fechaRegistro ASC")
    List<ListaEspera> findPendientesOrdenadasPorPrioridad();

    @Query("SELECT le FROM ListaEspera le WHERE le.estado = 'PENDIENTE' AND le.especialidad = :especialidad " +
           "ORDER BY CASE le.prioridad WHEN 'ALTA' THEN 1 WHEN 'MEDIA' THEN 2 WHEN 'BAJA' THEN 3 END, " +
           "le.fechaRegistro ASC")
    List<ListaEspera> findPendientesPorEspecialidadOrdenadasPorPrioridad(@Param("especialidad") String especialidad);

    List<ListaEspera> findByPacienteId(Long pacienteId);
}