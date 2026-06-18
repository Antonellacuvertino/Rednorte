package com.rednorte.servicio_notificaciones.repository;

import com.rednorte.servicio_notificaciones.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByOrderByCreatedAtDesc();
    List<Notification> findByReadFlagFalseOrderByCreatedAtDesc();
}
