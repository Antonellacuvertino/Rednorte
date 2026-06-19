# Seguridad e integracion

## JWT real

El BFF genera tokens JWT firmados explicitamente con HS256 mediante JJWT. El token contiene
correo, nombre, rol `MEDICO`, fecha de emision y expiracion. Un filtro valida
firma y vigencia en cada solicitud a `/bff/**`; el backend no confia solamente
en datos almacenados por React.

Cada uno de los seis microservicios funciona como OAuth2 Resource Server y
vuelve a validar firma, expiracion y rol. El BFF reenvia el encabezado Bearer
mediante OpenFeign; Reasignacion tambien lo propaga al llamar a Citas.

Las rutas `/auth/login` y `/auth/register` son publicas. El resto requiere:

```http
Authorization: Bearer <token>
```

Las claves se procesan con BCrypt. El registro exige dominio `@redsalud.cl` y
una clave de al menos ocho caracteres. En produccion se debe definir un
`JWT_SECRET` largo, aleatorio y administrado como secreto.

## Redis

El BFF usa Spring Cache con Redis. Se almacenan:

- listado de pacientes;
- detalle combinado de paciente y citas.

El TTL es de diez minutos. Al crear un paciente se invalida el listado para
evitar datos obsoletos. Redis se levanta desde `docker-compose.yml`.

## RabbitMQ

Las operaciones de creacion publican eventos `PACIENTE_CREADO` y
`CITA_CREADA`. El exchange distribuye cada evento a dos consumidores:

- exchange: `rednorte.events`;
- routing key: `audit.events`;
- cola de persistencia: `rednorte.audit`;
- cola de avisos: `rednorte.notifications`;
- serializacion: JSON.

`ms-auditoria` conserva el payload completo y `ms-notificaciones` crea un aviso
legible que puede marcarse como leido. Si RabbitMQ esta temporalmente fuera de servicio, el publicador registra el
error sin eliminar la respuesta ya obtenida del microservicio. En produccion se
recomienda outbox transaccional, reintentos y una dead-letter queue.

## Medidas adicionales

- Sesiones HTTP deshabilitadas en el BFF.
- CSRF deshabilitado porque la API usa Bearer tokens y no cookies de sesion.
- CORS centralizado para permitir el frontend configurado.
- Contrato OpenAPI versionado en `docs/openapi-rednorte.yaml`.
- DTO para no exponer entidades JPA directamente desde el BFF.
- Timeouts de cinco segundos en clientes OpenFeign.

## Swagger y OpenAPI

La documentacion es publica para permitir la exploracion, pero las operaciones
de negocio exigen JWT:

| Componente | Swagger UI |
| --- | --- |
| Pacientes | `http://localhost:8081/swagger-ui.html` |
| Citas | `http://localhost:8082/swagger-ui.html` |
| Lista de espera | `http://localhost:8083/swagger-ui.html` |
| Reasignacion | `http://localhost:8084/swagger-ui.html` |
| BFF | `http://localhost:8085/swagger-ui.html` |
| Auditoria | `http://localhost:8086/swagger-ui.html` |
| Notificaciones | `http://localhost:8087/swagger-ui.html` |

En Swagger se usa el boton **Authorize** con `Bearer <token>` o solo el token,
segun la version de la interfaz. El contrato JSON se publica en
`/v3/api-docs` de cada componente.
