# Informe de pruebas unitarias

## Resultado consolidado

Ejecucion verificada el 19 de junio de 2026:

| Componente | Herramienta | Pruebas | Cobertura de lineas |
| --- | --- | ---: | ---: |
| Frontend React | Vitest + V8 | 25 | 98,58% |
| BFF RedNorte | JUnit 5 + Mockito + JaCoCo | 17 | 100% |
| MS Pacientes | JUnit 5 + Mockito + JaCoCo | 4 | 100% |
| MS Citas | JUnit 5 + Mockito + JaCoCo | 7 | 96,08% |
| MS Lista Espera | JUnit 5 + Mockito + JaCoCo | 4 | 100% |
| MS Reasignacion | JUnit 5 + Mockito + JaCoCo | 6 | 100% |
| MS Auditoria | JUnit 5 + Mockito + JaCoCo | 5 | 100% |
| MS Notificaciones | JUnit 5 + Mockito + JaCoCo | 5 | 100% |

Resultado: 73 pruebas aprobadas, sin fallos.

El frontend obtuvo ademas 91,21% de ramas y 93,42% de funciones. Maven y
Vitest tienen umbrales automaticos de 90%; la ejecucion falla si un cambio
reduce la cobertura por debajo del objetivo.

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
integracion. Los paquetes de excepciones y el codigo de negocio si se miden.
