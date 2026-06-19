# Microservicio Auditoria

Consume eventos desde la cola `rednorte.audit`, conserva el payload original
en H2 y permite consultarlo mediante `GET /api/auditoria`.

```bat
mvnw.cmd spring-boot:run
mvnw.cmd test
```

Puerto: `8086`. Reporte JaCoCo: `target/site/jacoco/index.html`.
Swagger: `http://localhost:8086/swagger-ui.html`.
Los endpoints de consulta requieren JWT con rol `MEDICO`.
JaCoCo exige al menos 90%.
