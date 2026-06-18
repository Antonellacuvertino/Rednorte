package cl.duoc.rednorte.feign;

import cl.duoc.rednorte.dto.ReasignacionDTO;
import cl.duoc.rednorte.dto.ReasignacionRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "reasignacionClient", url = "${ms.reasignacion.url}")
public interface ReasignacionClient {
    @GetMapping("/api/reasignaciones")
    List<ReasignacionDTO> getAll();

    @PostMapping("/api/reasignaciones")
    ReasignacionDTO reprogramar(@RequestBody ReasignacionRequestDTO request);
}
