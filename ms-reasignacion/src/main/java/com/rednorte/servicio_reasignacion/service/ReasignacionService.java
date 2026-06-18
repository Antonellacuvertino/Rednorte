package com.rednorte.servicio_reasignacion.service;

import com.rednorte.servicio_reasignacion.dto.CitaResponse;
import com.rednorte.servicio_reasignacion.dto.ReasignacionRequest;
import com.rednorte.servicio_reasignacion.entity.Reasignacion;
import com.rednorte.servicio_reasignacion.repository.ReasignacionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ReasignacionService {
    private final ReasignacionRepository repository;
    private final RestTemplate restTemplate;
    private final String citasUrl;

    public ReasignacionService(
            ReasignacionRepository repository,
            RestTemplate restTemplate,
            @Value("${ms.citas.url:http://localhost:8082}") String citasUrl) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.citasUrl = citasUrl;
    }

    public List<Reasignacion> findAll() {
        return repository.findAllByOrderByFechaRegistroDesc();
    }

    public Reasignacion reprogramar(ReasignacionRequest request) {
        validate(request);
        try {
            CitaResponse cita = restTemplate.getForObject(
                    citasUrl + "/api/citas/" + request.citaId(),
                    CitaResponse.class);
            if (cita == null) {
                throw new IllegalArgumentException("La cita no existe");
            }

            restTemplate.put(
                    citasUrl + "/api/citas/" + request.citaId() + "/reprogramar",
                    Map.of("fecha", request.fechaNueva(), "hora", request.horaNueva()));

            Reasignacion reasignacion = new Reasignacion();
            reasignacion.setCitaId(cita.id());
            reasignacion.setPacienteId(cita.pacienteId());
            reasignacion.setFechaAnterior(cita.fecha());
            reasignacion.setHoraAnterior(cita.hora());
            reasignacion.setFechaNueva(request.fechaNueva());
            reasignacion.setHoraNueva(request.horaNueva());
            reasignacion.setMotivo(request.motivo().trim());
            reasignacion.setMedicoResponsable(request.medicoResponsable().trim());
            return repository.save(reasignacion);
        } catch (RestClientException ex) {
            throw new IllegalArgumentException("No fue posible actualizar la cita", ex);
        }
    }

    private void validate(ReasignacionRequest request) {
        if (request == null || request.citaId() == null
                || isBlank(request.fechaNueva()) || isBlank(request.horaNueva())
                || isBlank(request.motivo()) || isBlank(request.medicoResponsable())) {
            throw new IllegalArgumentException("Todos los datos de reasignacion son obligatorios");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
