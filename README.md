# Hospital Red Norte - Evaluacion 3 Fullstack III

Sistema de gestion hospitalaria basado en React, un BFF Spring Boot y seis
microservicios con persistencia independiente.

## Arquitectura

| Componente | Puerto | Responsabilidad |
| --- | ---: | --- |
| Frontend React | 5173 | Interfaz privada para personal medico |
| BFF RedNorte | 8085 | Seguridad JWT, cache, mensajeria y orquestacion |
| MS Pacientes | 8081 | Datos e historial de pacientes |
| MS Citas | 8082 | Agenda y tipos de citas |
| MS Lista Espera | 8083 | Priorizacion y estados de espera |
| MS Reasignacion | 8084 | Reprogramacion manual y trazabilidad de citas |
| MS Auditoria | 8086 | Consumo y persistencia de eventos |
| MS Notificaciones | 8087 | Avisos operativos generados por eventos |
| Redis | 6379 | Cache de consultas frecuentes |
| RabbitMQ | 5672/15672 | Eventos de auditoria y consola de administracion |

## Requisitos

- Java 17 o superior
- Node.js 20 o superior
- Docker Desktop

## Ejecucion

```bat
iniciar-servicios.bat
```

El script levanta Docker, instala NPM con `npm ci` cuando sea necesario,
inicia los seis microservicios, el BFF y React.

Acceso de demostracion:

- Correo: `medico@redsalud.cl`
- Clave: `salud1234`

La clave JWT debe configurarse mediante `JWT_SECRET` fuera de desarrollo.

## Pruebas y cobertura

```bat
ejecutar-pruebas.bat
```

Los reportes backend quedan en `<servicio>/target/site/jacoco/index.html` y el
reporte frontend en `frontend/coverage/index.html`.

## Orden de arranque manual

1. Redis y RabbitMQ.
2. Los seis microservicios.
3. BFF.
4. Frontend.

Los comandos exactos y la explicacion para defender la solucion estan en
`docs/guia_ejecucion_y_defensa.md`.

Para borrar dependencias, compilados, bases locales y reportes generados:

```bat
limpiar-generados.bat
```

## Documentacion de entrega

- [Arquitectura](docs/evaluacion3_arquitectura.md)
- [Persistencia](docs/evaluacion3_persistencia.md)
- [Seguridad e integracion](docs/evaluacion3_seguridad_integracion.md)
- [Informe de pruebas](docs/evaluacion3_pruebas.md)
- [Contrato OpenAPI](docs/openapi-rednorte.yaml)
- [Repositorios](docs/repositorios_evaluacion3.txt)
- [GitHub Flow y auditoria](docs/github_flow_y_auditoria.md)
- [Checklist de cumplimiento](docs/checklist_evaluacion3_cumplimiento.md)
