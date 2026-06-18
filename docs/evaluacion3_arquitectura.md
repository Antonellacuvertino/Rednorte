# Arquitectura de microservicios

## Vista de componentes

```mermaid
flowchart LR
    U[Personal medico] --> F[React + Vite]
    F -->|REST + Bearer JWT| B[BFF Spring Boot]
    B -->|OpenFeign REST| P[MS Pacientes]
    B -->|OpenFeign REST| C[MS Citas]
    B -->|OpenFeign REST| L[MS Lista Espera]
    B -->|OpenFeign REST| R[MS Reasignacion]
    B -->|OpenFeign REST| A[MS Auditoria]
    B -->|OpenFeign REST| N[MS Notificaciones]
    B <--> D[(Redis)]
    B -->|Eventos de auditoria| Q[(RabbitMQ)]
    Q -->|rednorte.audit| A
    Q -->|rednorte.notifications| N
    P --> HP[(H2 Pacientes)]
    C --> HC[(H2 Citas)]
    L --> HL[(H2 Lista Espera)]
    R --> HR[(H2 Reasignacion)]
    A --> HA[(H2 Auditoria)]
    N --> HN[(H2 Notificaciones)]
```

El frontend solo se comunica con el BFF. Esto evita exponer directamente los
microservicios y centraliza autenticacion, autorizacion, CORS, cache y
auditoria. Cada servicio conserva su propia base de datos para reducir el
acoplamiento.

## Flujo autenticado

```mermaid
sequenceDiagram
    actor M as Medico
    participant F as Frontend
    participant B as BFF
    participant R as Redis
    participant P as MS Pacientes
    participant Q as RabbitMQ

    M->>F: Inicia sesion
    F->>B: POST /auth/login
    B-->>F: JWT firmado
    F->>B: GET /bff/pacientes + Bearer JWT
    B->>R: Buscar cache
    alt Cache disponible
        R-->>B: Pacientes
    else Cache vacia
        B->>P: GET /api/pacientes
        P-->>B: Pacientes persistidos
        B->>R: Guardar por 10 minutos
    end
    B-->>F: JSON
    F->>B: POST /bff/pacientes
    B->>P: Persistir paciente
    B->>R: Invalidar cache
    B->>Q: Evento PACIENTE_CREADO
    B-->>F: Paciente creado
```

## Decisiones

- BFF: contrato unico para React y ocultamiento de topologia interna.
- OpenFeign: clientes REST declarativos entre BFF y microservicios.
- Redis: disminuye lecturas repetidas y se invalida al crear pacientes.
- RabbitMQ: entrega cada evento a las colas independientes de auditoria y
  notificaciones sin bloquear la operacion principal.
- JWT: autenticacion sin sesion de servidor y autorizacion por rol.
- Base de datos por servicio: propiedad y evolucion independiente de datos.
