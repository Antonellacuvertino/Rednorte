package com.rednorte.servicio_reasignacion.repository;

import com.rednorte.servicio_reasignacion.entity.Reasignacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReasignacionRepository extends JpaRepository<Reasignacion, Long> {
    List<Reasignacion> findAllByOrderByFechaRegistroDesc();
}
