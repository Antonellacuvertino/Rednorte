# Microservicio Citas

Gestiona la agenda medica y crea citas segun su tipo mediante Factory.

```bat
mvnw.cmd spring-boot:run
mvnw.cmd test
```

Puerto: `8082`. API: `/api/citas`.

Swagger: `http://localhost:8082/swagger-ui.html`.
Los endpoints de negocio requieren JWT con rol `MEDICO`.
JaCoCo exige al menos 90% y genera `target/site/jacoco/index.html`.
