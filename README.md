# Hospital Red Norte - Gestion RedSalud

Sistema fullstack privado para gestion hospitalaria, construido con React, BFF Spring Boot y microservicios independientes.

## Modulos

- `frontend/`: panel privado React + Vite para medicos RedSalud.
- `bff-rednorte/`: Backend For Frontend en puerto `8085`.
- `ms-pacientes/`: gestion de pacientes en puerto `8081`.
- `ms-citas/`: agenda medica en puerto `8082`.
- `ms-lista-espera/`: lista de espera priorizada en puerto `8083`.
- `ms-reasignacion/`: reglas de reasignacion en puerto `8084`.
- `maven-archetypes/backend-archetype/`: arquetipo Maven base.
- `docs/`: documentacion para evaluacion, patrones, endpoints y evidencia.

## Acceso privado

La aplicacion ya no muestra secciones publicas. Primero se debe registrar un medico usando un correo que termine en `@redsalud.cl`; luego se inicia sesion y se habilita el panel completo.

## Ejecucion rapida

```bash
iniciar-servicios.bat
```

URLs:

- Frontend: `http://localhost:5173`
- BFF: `http://localhost:8085`
- Pacientes: `http://localhost:8081`
- Citas: `http://localhost:8082`
- Lista de espera: `http://localhost:8083`
- Reasignacion: `http://localhost:8084`

## Validacion

```bash
cd frontend
npm test
npm run build

cd ../bff-rednorte
mvnw.cmd test

cd ../ms-lista-espera
mvnw.cmd test

cd ../ms-reasignacion
mvnw.cmd test
```

## Documentacion clave

- `docs/actualizacion_evaluacion_2.md`
- `docs/checklist_evaluacion.md`
- `docs/analisis_patrones_arquetipos.md`
- `docs/plan_branching.md`
