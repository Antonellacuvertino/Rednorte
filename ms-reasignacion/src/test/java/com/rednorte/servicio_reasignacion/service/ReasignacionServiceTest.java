package com.rednorte.servicio_reasignacion.service;

import com.rednorte.servicio_reasignacion.dto.CitaResponse;
import com.rednorte.servicio_reasignacion.dto.ReasignacionRequest;
import com.rednorte.servicio_reasignacion.entity.Reasignacion;
import com.rednorte.servicio_reasignacion.exception.ResourceNotFoundException;
import com.rednorte.servicio_reasignacion.repository.ReasignacionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReasignacionServiceTest {
    @Test
    void actualizaCitaYGuardaTrazabilidad() {
        ReasignacionRepository repository = mock(ReasignacionRepository.class);
        RestTemplate restTemplate = mock(RestTemplate.class);
        ReasignacionService service = new ReasignacionService(repository, restTemplate, "http://citas");
        CitaResponse cita = new CitaResponse(7L, 3L, "2026-07-01", "09:00", "CARDIOLOGIA", "GENERAL", 1);
        when(restTemplate.getForObject("http://citas/api/citas/7", CitaResponse.class)).thenReturn(cita);
        when(repository.save(any(Reasignacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reasignacion result = service.reprogramar(new ReasignacionRequest(
                7L, "2026-07-01", "11:30", "Paciente llego atrasado", "Dra. Norte"));

        assertEquals("09:00", result.getHoraAnterior());
        assertEquals("11:30", result.getHoraNueva());
        verify(restTemplate).put(
                "http://citas/api/citas/7/reprogramar",
                java.util.Map.of("fecha", "2026-07-01", "hora", "11:30"));
    }

    @Test
    void validaCamposYListaHistorial() {
        ReasignacionRepository repository = mock(ReasignacionRepository.class);
        ReasignacionService service = new ReasignacionService(repository, mock(RestTemplate.class), "http://citas");
        when(repository.findAllByOrderByFechaRegistroDesc()).thenReturn(List.of());

        assertEquals(0, service.findAll().size());
        assertThrows(IllegalArgumentException.class, () -> service.reprogramar(
                new ReasignacionRequest(null, "", "", "", "")));
    }

    @Test
    void rechazaCitaInexistenteYFallaDeIntegracion() {
        ReasignacionRepository repository = mock(ReasignacionRepository.class);
        RestTemplate restTemplate = mock(RestTemplate.class);
        ReasignacionService service = new ReasignacionService(repository, restTemplate, "http://citas");
        ReasignacionRequest request = new ReasignacionRequest(
                9L, "2026-07-01", "11:30", "Atraso", "Dra. Norte");

        when(restTemplate.getForObject("http://citas/api/citas/9", CitaResponse.class)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> service.reprogramar(request));

        when(restTemplate.getForObject("http://citas/api/citas/9", CitaResponse.class))
                .thenThrow(new RestClientException("sin conexion"));
        assertThrows(IllegalArgumentException.class, () -> service.reprogramar(request));

        reset(restTemplate);
        when(restTemplate.getForObject("http://citas/api/citas/9", CitaResponse.class))
                .thenThrow(HttpClientErrorException.create(
                        HttpStatus.NOT_FOUND, "No existe", null, null, null));
        assertThrows(ResourceNotFoundException.class, () -> service.reprogramar(request));
    }
}
