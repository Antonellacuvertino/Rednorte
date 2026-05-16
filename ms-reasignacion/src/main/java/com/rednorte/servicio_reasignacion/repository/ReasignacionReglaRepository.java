package com.rednorte.servicio_reasignacion.repository;

import com.rednorte.servicio_reasignacion.entity.ReasignacionRegla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReasignacionReglaRepository extends JpaRepository<ReasignacionRegla, Long> {

    List<ReasignacionRegla> findByEspecialidadAndActiva(String especialidad, boolean activa);

    List<ReasignacionRegla> findByReglaTipoAndActiva(String reglaTipo, boolean activa);

    List<ReasignacionRegla> findByActiva(boolean activa);
}
