# Informe de pruebas unitarias

## Resultado consolidado

Ejecucion verificada el 18 de junio de 2026:

| Componente | Herramienta | Pruebas | Cobertura de lineas |
| --- | --- | ---: | ---: |
| Frontend React | Vitest + V8 | 15 | 79,91% |
| BFF RedNorte | JUnit 5 + Mockito + JaCoCo | 12 | 74,79% |
| MS Pacientes | JUnit 5 + Mockito + JaCoCo | 3 | 100% |
| MS Citas | JUnit 5 + Mockito + JaCoCo | 4 | 93,94% |
| MS Lista Espera | JUnit 5 + Mockito + JaCoCo | 5 | 100% |
| MS Reasignacion | JUnit 5 + Mockito + JaCoCo | 4 | 92,31% |
| MS Auditoria | JUnit 5 + Mockito + JaCoCo | 3 | 80,95% |
| MS Notificaciones | JUnit 5 + Mockito + JaCoCo | 4 | 82,14% |

Resultado: 50 pruebas aprobadas, sin fallos.

## Casos cubiertos

- Login, registro institucional, contrasena incorrecta y usuario duplicado.
- Generacion, lectura, firma y expiracion del JWT.
- Publicacion RabbitMQ y tolerancia ante broker no disponible.
- Consumo y persistencia de eventos en dos colas independientes.
- Listado de auditoria y cambio de estado de notificaciones.
- Lectura, creacion y respuestas 404 del BFF.
- CRUD de pacientes, citas y espera, junto con reprogramacion manual trazable.
- Renderizado de componentes React, navegacion y formularios.
- Cliente HTTP frontend, cabecera Bearer y manejo de errores.

## Ejecucion

Desde la raiz:

```bat
ejecutar-pruebas.bat
```

O por componente:

```bat
cd frontend
npm.cmd run test:coverage

cd ..\bff-rednorte
mvnw.cmd clean test
```

El mismo comando Maven se ejecuta en cada microservicio.

## Reportes

- Frontend: `frontend/coverage/index.html`
- BFF: `bff-rednorte/target/site/jacoco/index.html`
- Pacientes: `ms-pacientes/target/site/jacoco/index.html`
- Citas: `ms-citas/target/site/jacoco/index.html`
- Lista espera: `ms-lista-espera/target/site/jacoco/index.html`
- Reasignacion: `ms-reasignacion/target/site/jacoco/index.html`
- Auditoria: `ms-auditoria/target/site/jacoco/index.html`
- Notificaciones: `ms-notificaciones/target/site/jacoco/index.html`

JaCoCo excluye clases de configuracion, DTO, entidades y clases de arranque
para que la metrica represente controladores, servicios, seguridad y logica de
integracion. No se excluye codigo de negocio.
