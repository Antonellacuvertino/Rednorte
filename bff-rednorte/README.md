# BFF RedNorte

Punto de entrada seguro para el frontend. Orquesta los microservicios mediante
OpenFeign y concentra autenticacion, cache y publicacion de eventos.

## Funciones

- JWT firmado con HMAC y expiracion de 120 minutos.
- Acceso a `/bff/**` limitado al rol `MEDICO`.
- Registro institucional restringido a correos `@redsalud.cl`.
- Claves almacenadas con BCrypt.
- Cache Redis para pacientes y detalle paciente-citas.
- Eventos de auditoria enviados a RabbitMQ.
- Swagger/OpenAPI en `http://localhost:8085/swagger-ui.html`.

## Ejecucion

Primero inicie Redis, RabbitMQ y los seis microservicios:

```bat
docker compose up -d
cd bff-rednorte
mvnw.cmd spring-boot:run
```

Variables disponibles:

```text
JWT_SECRET
DEMO_USER_NAME
DEMO_USER_EMAIL
DEMO_USER_PASSWORD
REDIS_HOST
REDIS_PORT
RABBITMQ_HOST
RABBITMQ_PORT
RABBITMQ_USERNAME
RABBITMQ_PASSWORD
```

## Pruebas

```bat
mvnw.cmd clean test
```

Reporte JaCoCo: `target/site/jacoco/index.html`.
La compilacion falla si la cobertura de lineas es inferior a 90%.
