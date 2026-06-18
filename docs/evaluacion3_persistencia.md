# Descripcion de la persistencia

## Estrategia

Los seis microservicios de dominio usan Spring Data JPA. Cada uno define sus
entidades, repositorios y archivo `application.properties`, manteniendo la
persistencia separada por contexto:

| Servicio | Datos principales | Recurso |
| --- | --- | --- |
| MS Pacientes | Identidad e historial clinico | `PacienteRepository` |
| MS Citas | Fecha, hora, paciente, especialidad y prioridad | `CitaRepository` |
| MS Lista Espera | Prioridad, estado y fechas de atencion | `ListaEsperaRepository` |
| MS Reasignacion | Horario anterior, horario nuevo, motivo y responsable | `ReasignacionRepository` |
| MS Auditoria | Tipo, fecha y payload original del evento | `AuditEventRepository` |
| MS Notificaciones | Avisos, estado leido y fecha | `NotificationRepository` |

En el ambiente academico se usa H2 en modo archivo. Esto permite conservar los
datos entre reinicios sin instalar un motor externo. JPA crea o actualiza las
tablas de acuerdo con las entidades configuradas.

## Flujo de datos

1. React envia JSON al BFF.
2. El BFF valida el JWT y transforma la solicitud a DTO.
3. OpenFeign invoca el endpoint del microservicio propietario.
4. El controlador delega al servicio de negocio.
5. El servicio usa un repositorio JPA.
6. Hibernate traduce la operacion a SQL y H2 confirma la transaccion.
7. La respuesta vuelve al frontend como JSON.

No se comparten tablas entre microservicios. Las relaciones entre citas y
pacientes se representan mediante `pacienteId`, evitando dependencias JPA entre
bases de datos.

## Evolucion productiva

Para produccion, cada URL H2 puede reemplazarse por PostgreSQL o MySQL mediante
variables de entorno y el driver correspondiente. Se recomienda incorporar
Flyway para versionar el esquema, cifrar respaldos y utilizar cuentas con
privilegios minimos.
