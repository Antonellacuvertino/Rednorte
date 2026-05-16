# Checklist de Evaluacion Parcial 2

## Encargo grupal

- Frontend NPM: cumple. Existe `frontend/package.json`, estructura `src/`, componentes React, README, scripts `dev`, `build` y `test`. Incluye login/registro privado con dominio `@redsalud.cl`.
- Backend For Frontend: cumple. Existe `bff-rednorte`, expone endpoints para pacientes, citas, lista de espera y reasignacion, integrando los microservicios con OpenFeign.
- Microservicios: cumple. Existen `ms-pacientes` en puerto `8081`, `ms-citas` en puerto `8082`, `ms-lista-espera` en puerto `8083` y `ms-reasignacion` en puerto `8084`.
- Arquetipo Maven: cumple. Existe `maven-archetypes/backend-archetype` con `pom.xml`, metadatos y README.
- Patrones de diseno: cumple. Se evidencian Facade/Service y Container/Presentational en frontend; Repository, DTO/Adapter, Factory Method y BFF como Facade en backend.
- Patrones arquitectonicos: cumple. Arquitectura BFF + microservicios, con separacion por responsabilidad.
- Pruebas unitarias: cumple base. Frontend, BFF y microservicios tienen pruebas ejecutables. Conviene mencionar que la cobertura es inicial, no exhaustiva.
- Branching: documentado en `docs/plan_branching.md`. Falta reemplazar la recomendacion por evidencia real si el docente exige historial de ramas, merges o conflictos desde GitHub.
- Repositorios: documentado en `docs/repositorios.txt`. Falta reemplazar `tu-usuario` por URLs reales antes de entregar.

## Validacion tecnica realizada

- `npm.cmd test`: test frontend OK.
- `npm.cmd run build`: build frontend OK.
- `bff-rednorte`: tests OK.
- `ms-lista-espera`: tests OK.
- `ms-reasignacion`: tests OK.
- `ms-pacientes`: test base OK.
- `ms-citas`: tests base OK.
- BFF validado en vivo en `http://127.0.0.1:8085/bff/pacientes`.
- Endpoints agregados validados: `/bff/lista-espera/pendientes` y `/bff/reasignacion/reglas`.

## Riesgos antes de entregar

- Actualizar enlaces reales de GitHub en `docs/repositorios.txt`.
- Exportar `analisis_patrones_arquetipos.md` y `plan_branching.md` a PDF si la pauta lo exige en PDF.
- Verificar que el ZIP/RAR no incluya `node_modules`, `target` ni archivos pesados innecesarios.
- Si se defiende branching, mostrar capturas o historial real de ramas, commits, merges y resolucion de conflictos.
