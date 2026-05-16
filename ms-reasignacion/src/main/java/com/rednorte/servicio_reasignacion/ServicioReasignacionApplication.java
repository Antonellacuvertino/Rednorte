package com.rednorte.servicio_reasignacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServicioReasignacionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServicioReasignacionApplication.class, args);
    }
}
