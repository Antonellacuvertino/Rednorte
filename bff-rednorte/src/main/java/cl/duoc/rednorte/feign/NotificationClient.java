package cl.duoc.rednorte.feign;

import cl.duoc.rednorte.dto.NotificationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "notificationClient", url = "${ms.notificaciones.url}")
public interface NotificationClient {
    @GetMapping("/api/notificaciones")
    List<NotificationDTO> findAll(@RequestParam(name = "noLeidas") boolean unreadOnly);

    @PutMapping("/api/notificaciones/{id}/leer")
    NotificationDTO markAsRead(@PathVariable("id") Long id);
}
