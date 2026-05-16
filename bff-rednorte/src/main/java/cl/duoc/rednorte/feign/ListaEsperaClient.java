package cl.duoc.rednorte.feign;

import cl.duoc.rednorte.dto.ListaEsperaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "listaEsperaClient", url = "${ms.lista-espera.url}")
public interface ListaEsperaClient {
    @GetMapping("/api/lista-espera")
    List<ListaEsperaDTO> getAll();

    @GetMapping("/api/lista-espera/pendientes")
    List<ListaEsperaDTO> getPendientes();

    @PostMapping("/api/lista-espera")
    ListaEsperaDTO create(@RequestBody ListaEsperaDTO listaEspera);

    @PutMapping("/api/lista-espera/{id}/atender")
    ListaEsperaDTO atender(@PathVariable("id") Long id);

    @PutMapping("/api/lista-espera/{id}/cancelar")
    ListaEsperaDTO cancelar(@PathVariable("id") Long id);
}
