# Microservicio Pacientes

Gestiona fichas e historial clinico mediante Spring Data JPA y H2.

```bat
mvnw.cmd spring-boot:run
mvnw.cmd test
```

Puerto: `8081`. API: `/api/pacientes`.

Swagger: `http://localhost:8081/swagger-ui.html`.
Los endpoints de negocio requieren JWT con rol `MEDICO`.
JaCoCo exige al menos 90% y genera `target/site/jacoco/index.html`.
