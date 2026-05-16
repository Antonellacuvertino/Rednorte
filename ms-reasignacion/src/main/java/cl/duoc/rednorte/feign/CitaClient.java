package cl.duoc.rednorte.feign;

import cl.duoc.rednorte.dto.CitaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "citaClient", url = "${ms.citas.url}")
public interface CitaClient {
    @GetMapping("/api/citas")
    List<CitaDTO> getAllCitas();

    @GetMapping("/api/citas/paciente/{pacienteId}")
    List<CitaDTO> getCitasByPacienteId(@PathVariable("pacienteId") Long pacienteId);
}
