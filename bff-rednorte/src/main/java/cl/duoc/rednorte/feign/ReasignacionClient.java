package cl.duoc.rednorte.feign;

import cl.duoc.rednorte.dto.ReasignacionReglaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "reasignacionClient", url = "${ms.reasignacion.url}")
public interface ReasignacionClient {
    @GetMapping("/api/reasignacion/reglas")
    List<ReasignacionReglaDTO> getReglas();

    @PostMapping("/api/reasignacion/reglas")
    ReasignacionReglaDTO createRegla(@RequestBody ReasignacionReglaDTO regla);

    @PostMapping("/api/reasignacion/inicializar")
    String inicializar();

    @PostMapping("/api/reasignacion/ejecutar")
    String ejecutar();
}
