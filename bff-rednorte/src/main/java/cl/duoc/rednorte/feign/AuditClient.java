package cl.duoc.rednorte.feign;

import cl.duoc.rednorte.dto.AuditEventDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "auditClient", url = "${ms.auditoria.url}")
public interface AuditClient {
    @GetMapping("/api/auditoria")
    List<AuditEventDTO> findAll(@RequestParam(name = "tipo", required = false) String type);
}
