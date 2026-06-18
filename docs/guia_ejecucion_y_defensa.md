# Guia de ejecucion y defensa

## Como explicar la solucion

El sistema tiene un frontend React, un BFF y seis microservicios de dominio.
React solo conoce al BFF. El BFF valida el JWT, aplica el rol `MEDICO`, usa
Redis para cache y OpenFeign para comunicarse por REST con cada servicio.

Los servicios son:

1. Pacientes: ficha e historial clinico.
2. Citas: agenda medica.
3. Lista de espera: prioridad y estado de atencion.
4. Reasignacion: permite que un medico cambie manualmente el horario de una
   cita y conserva la trazabilidad del cambio.
5. Auditoria: consume RabbitMQ y conserva evidencia de cada evento.
6. Notificaciones: consume una cola independiente y crea avisos operativos.

Cuando se crea un paciente o una cita, el BFF publica un evento en el exchange
`rednorte.events`. RabbitMQ entrega una copia a `rednorte.audit` y otra a
`rednorte.notifications`. Por eso ambos servicios reaccionan sin llamarse
directamente y sin retrasar la respuesta al usuario.

La reasignacion es manual: el medico selecciona una cita, registra nueva fecha,
nueva hora y motivo. El servicio consulta el horario anterior en `ms-citas`,
actualiza la agenda y conserva ambos horarios junto al responsable.

Cada microservicio tiene su propia base H2. Esta separacion evita compartir
tablas y permite reemplazar cada base por PostgreSQL de forma independiente.

## Seguridad

- Login y registro se realizan en el BFF.
- Las claves se comparan con BCrypt.
- El JWT lleva identidad, rol, emision y expiracion.
- `/bff/**` exige `Authorization: Bearer <token>`.
- La clave de firma se configura con `JWT_SECRET`.
- El frontend nunca guarda la contrasena.

## Pruebas

Backend usa JUnit 5, Mockito y JaCoCo. Frontend usa Vitest, Testing Library y
V8 Coverage. Las pruebas cubren controladores, servicios, JWT, clientes HTTP,
componentes React y consumidores RabbitMQ.

## Demostracion sugerida

1. Iniciar el sistema con `iniciar-servicios.bat`.
2. Entrar con `medico@redsalud.cl` y `salud1234`.
3. Crear un paciente y una cita.
4. Abrir Lista de espera y mostrar auditoria y notificaciones.
5. Abrir RabbitMQ en `http://localhost:15672`.
6. Mostrar un reporte `target/site/jacoco/index.html`.

## Ejecucion limpia manual

Desde la raiz del proyecto:

```bat
docker compose up -d
cd frontend
npm.cmd ci
cd ..
```

En terminales separadas:

```bat
cd ms-pacientes && mvnw.cmd spring-boot:run
cd ms-citas && mvnw.cmd spring-boot:run
cd ms-lista-espera && mvnw.cmd spring-boot:run
cd ms-reasignacion && mvnw.cmd spring-boot:run
cd ms-auditoria && mvnw.cmd spring-boot:run
cd ms-notificaciones && mvnw.cmd spring-boot:run
cd bff-rednorte && mvnw.cmd spring-boot:run
cd frontend && npm.cmd install && npm.cmd run dev
```

Para detener infraestructura:

```bat
docker compose down
```

Para pruebas:

```bat
ejecutar-pruebas.bat
```

Para volver a un estado completamente limpio:

```bat
limpiar-generados.bat
iniciar-servicios.bat
```
