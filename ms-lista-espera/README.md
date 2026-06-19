# Microservicio Lista de Espera

Gestiona pacientes pendientes, prioridades y estados de atencion.

```bat
mvnw.cmd spring-boot:run
mvnw.cmd test
```

Puerto: `8083`. API: `/api/lista-espera`.

Swagger: `http://localhost:8083/swagger-ui.html`.
Los endpoints de negocio requieren JWT con rol `MEDICO`.
JaCoCo exige al menos 90% y genera `target/site/jacoco/index.html`.
