package com.rednorte.servicio_auditoria.repository;

import com.rednorte.servicio_auditoria.entity.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {
    List<AuditEvent> findByEventTypeOrderByOccurredAtDesc(String eventType);
    List<AuditEvent> findAllByOrderByOccurredAtDesc();
}
