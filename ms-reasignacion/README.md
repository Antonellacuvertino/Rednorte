# Microservicio Reasignacion

Reprograma citas manualmente, actualiza la agenda y conserva fecha, hora,
motivo y medico responsable antes y despues del cambio.

```bat
mvnw.cmd spring-boot:run
mvnw.cmd test
```

Puerto: `8084`. API: `/api/reasignaciones`.

Swagger: `http://localhost:8084/swagger-ui.html`.
Los endpoints de negocio requieren JWT con rol `MEDICO`.
El token se propaga al microservicio Citas durante la reprogramacion.
JaCoCo exige al menos 90% y genera `target/site/jacoco/index.html`.
