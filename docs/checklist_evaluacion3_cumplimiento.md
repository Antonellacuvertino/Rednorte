# Checklist de cumplimiento Evaluacion 3

Revision tecnica: 19 de junio de 2026.

| Requerimiento | Estado | Evidencia |
| --- | --- | --- |
| BFF y seis microservicios | Cumple | Puertos 8081-8087 y arquitectura documentada |
| JavaDoc en Service y Controller | Cumple | Clases y operaciones publicas documentadas |
| Swagger/OpenAPI por servicio | Cumple | `/swagger-ui.html` y `/v3/api-docs` |
| Paquete de excepciones | Cumple | `exception/`, error uniforme 400/404/500 |
| JWT firmado y con expiracion | Cumple | HS256, `iat`, `exp`, secreto por entorno |
| Endpoints privados con Security | Cumple | BFF y seis Resource Servers stateless |
| Cobertura backend >= 90% | Cumple | 96,08%-100%, regla JaCoCo obligatoria |
| Cobertura frontend >= 90% | Cumple | lineas 98,58%, ramas 91,21%, funciones 93,42% |
| Redis y TTL | Cumple | cache BFF, TTL 10 minutos, invalidacion al crear |
| RabbitMQ | Cumple | auditoria y notificaciones asincronas |
| Persistencia independiente | Cumple | una base H2 por microservicio |
| GitHub Flow | Cumple en codigo | rama feature, plantillas, CI y guia |
| Auditoria avanzada | Cumple en codigo | CodeQL, Dependabot, Dependency Review, CODEOWNERS |
| Repositorios independientes | Preparado | enlaces y modulos listos; verificar creacion remota |
| Proteccion de `main` | Requiere GitHub | activar Ruleset segun guia antes de la auditoria |

## Evidencia de cobertura

- Frontend: `frontend/coverage/index.html`.
- Backend: `<servicio>/target/site/jacoco/index.html`.
- Comando consolidado: `ejecutar-pruebas.bat`.

## Evidencia de seguridad

1. Obtener token en `POST http://localhost:8085/auth/login`.
2. Invocar un endpoint sin token y comprobar `401`.
3. Invocarlo con `Authorization: Bearer <token>` y comprobar respuesta exitosa.
4. Abrir Swagger de un microservicio y autorizar el mismo JWT.
