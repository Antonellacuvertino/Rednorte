package cl.duoc.rednorte.feign;

import cl.duoc.rednorte.dto.PacienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "pacienteClient", url = "${ms.pacientes.url}")
public interface PacienteClient {
    @GetMapping("/api/pacientes/{id}")
    PacienteDTO getPacienteById(@PathVariable("id") Long id);

    @GetMapping("/api/pacientes")
    List<PacienteDTO> getAllPacientes();

    @PostMapping("/api/pacientes")
    PacienteDTO createPaciente(@RequestBody PacienteDTO paciente);
}
