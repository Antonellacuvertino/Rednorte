package com.rednorte.servicio_pacientes.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI pacientesOpenApi() {
        return securedApi("Microservicio Pacientes", "Gestion de pacientes de RedNorte");
    }

    private OpenAPI securedApi(String title, String description) {
        String scheme = "bearerAuth";
        return new OpenAPI()
                .info(new Info().title(title).description(description).version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(scheme))
                .components(new Components().addSecuritySchemes(scheme, new SecurityScheme()
                        .name(scheme)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}
