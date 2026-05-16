package com.rednorte.servicio_reasignacion.service;

import com.rednorte.servicio_reasignacion.entity.ReasignacionRegla;
import com.rednorte.servicio_reasignacion.repository.ReasignacionReglaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ReasignacionService {

    @Autowired
    private ReasignacionReglaRepository reglaRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String LISTA_ESPERA_URL = "http://localhost:8083/api/lista-espera";
    private final String CITAS_URL = "http://localhost:8082/api/citas";

    // Ejecutar cada 5 minutos
    @Scheduled(fixedRate = 300000)
    public void ejecutarReasignacionAutomatica() {
        System.out.println("Ejecutando reasignación automática: " + LocalDateTime.now());

        List<ReasignacionRegla> reglasActivas = reglaRepository.findByActiva(true);

        for (ReasignacionRegla regla : reglasActivas) {
            procesarRegla(regla);
        }
    }

    private void procesarRegla(ReasignacionRegla regla) {
        try {
            switch (regla.getReglaTipo()) {
                case "TIEMPO_ESPERA":
                    procesarReglaTiempoEspera(regla);
                    break;
                case "CAPACIDAD_MAXIMA":
                    procesarReglaCapacidadMaxima(regla);
                    break;
                case "PRIORIDAD":
                    procesarReglaPrioridad(regla);
                    break;
                default:
                    System.out.println("Tipo de regla no reconocido: " + regla.getReglaTipo());
            }
        } catch (Exception e) {
            System.err.println("Error procesando regla " + regla.getId() + ": " + e.getMessage());
        }
    }

    private void procesarReglaTiempoEspera(ReasignacionRegla regla) {
        // Obtener pacientes en lista de espera por especialidad
        String url = LISTA_ESPERA_URL + "/pendientes/" + regla.getEspecialidad();
        List<?> listaEspera = restTemplate.getForObject(url, List.class);

        if (listaEspera != null && !listaEspera.isEmpty()) {
            int tiempoMaximo = Integer.parseInt(regla.getValor());

            // Lógica para reasignar citas basadas en tiempo de espera
            // (Implementación simplificada)
            System.out.println("Procesando regla de tiempo de espera para " + regla.getEspecialidad() +
                             ": " + listaEspera.size() + " pacientes esperando");
        }
    }

    private void procesarReglaCapacidadMaxima(ReasignacionRegla regla) {
        // Obtener citas disponibles para la especialidad
        String url = CITAS_URL + "/disponibles/" + regla.getEspecialidad();
        List<?> citasDisponibles = restTemplate.getForObject(url, List.class);

        if (citasDisponibles != null) {
            int capacidadMaxima = Integer.parseInt(regla.getValor());

            if (citasDisponibles.size() < capacidadMaxima) {
                // Reasignar pacientes de lista de espera
                System.out.println("Capacidad baja para " + regla.getEspecialidad() +
                                 ": " + citasDisponibles.size() + "/" + capacidadMaxima);
            }
        }
    }

    private void procesarReglaPrioridad(ReasignacionRegla regla) {
        // Lógica para reasignar basada en prioridades
        String url = LISTA_ESPERA_URL + "/pendientes/" + regla.getEspecialidad();
        List<?> listaEspera = restTemplate.getForObject(url, List.class);

        if (listaEspera != null && !listaEspera.isEmpty()) {
            // Procesar pacientes de alta prioridad primero
            System.out.println("Procesando prioridades para " + regla.getEspecialidad() +
                             ": " + listaEspera.size() + " pacientes");
        }
    }

    public void inicializarReglasPorDefecto() {
        // Crear reglas por defecto si no existen
        if (reglaRepository.count() == 0) {
            reglaRepository.save(new ReasignacionRegla("CARDIOLOGIA", "TIEMPO_ESPERA", "30",
                    "Reasignar si espera más de 30 minutos"));
            reglaRepository.save(new ReasignacionRegla("PEDIATRIA", "CAPACIDAD_MAXIMA", "10",
                    "Mantener máximo 10 citas por día"));
            reglaRepository.save(new ReasignacionRegla("TRAUMATOLOGIA", "PRIORIDAD", "ALTA",
                    "Priorizar pacientes de alta urgencia"));
        }
    }
}
