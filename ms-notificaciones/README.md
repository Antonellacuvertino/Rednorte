# Microservicio Notificaciones

Consume la cola `rednorte.notifications`, crea notificaciones operativas y
permite listarlas o marcarlas como leidas.

```bat
mvnw.cmd spring-boot:run
mvnw.cmd test
```

Puerto: `8087`. Reporte JaCoCo: `target/site/jacoco/index.html`.
Swagger: `http://localhost:8087/swagger-ui.html`.
Los endpoints de consulta requieren JWT con rol `MEDICO`.
JaCoCo exige al menos 90%.
