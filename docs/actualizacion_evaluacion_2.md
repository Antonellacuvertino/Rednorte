# Actualizacion Evaluacion Parcial 2

## Resumen funcional

Se ajusto el proyecto a una gestion privada de Hospital Red Norte / RedSalud. La aplicacion ya no expone categorias publicas antes de autenticar: primero muestra registro e inicio de sesion para medicos, validando que el correo termine en `@redsalud.cl`.

## Cambios Frontend

- Pantalla privada de autenticacion con dos acciones: `Iniciar sesion` y `Registrarse`.
- Registro de medico con validacion de dominio institucional `@redsalud.cl`.
- Redireccion posterior al login hacia el panel de pacientes.
- Reemplazo visual de marca por logo Hospital Red Norte.
- Navegacion actualizada: la categoria `Microservicios` fue reemplazada por `Lista de espera`.
- Vista de lista de espera con formularios para agregar pacientes, atender/cancelar registros y administrar reglas de reasignacion.

## Cambios Backend y BFF

- `ms-lista-espera`: se corrigio el guardado de registros completando automaticamente `fechaRegistro`, `estado`, `prioridad` y `especialidad` antes de persistir.
- `bff-rednorte`: mantiene endpoints unificados para pacientes, citas, lista de espera y reasignacion.
- `ms-reasignacion`: reglas disponibles desde el BFF para inicializar, crear y ejecutar reasignacion.

## Endpoints relevantes

| Servicio | Metodo | Endpoint | Uso |
| --- | --- | --- | --- |
| BFF | GET | `/bff/pacientes` | Listar pacientes |
| BFF | POST | `/bff/pacientes` | Crear paciente |
| BFF | GET | `/bff/citas` | Listar citas |
| BFF | POST | `/bff/citas` | Crear cita |
| BFF | GET | `/bff/lista-espera/pendientes` | Ver cola priorizada |
| BFF | POST | `/bff/lista-espera` | Agregar paciente a espera |
| BFF | PUT | `/bff/lista-espera/{id}/atender` | Marcar atendido |
| BFF | PUT | `/bff/lista-espera/{id}/cancelar` | Cancelar registro |
| BFF | GET | `/bff/reasignacion/reglas` | Listar reglas |
| BFF | POST | `/bff/reasignacion/reglas` | Crear regla |
| BFF | POST | `/bff/reasignacion/inicializar` | Crear reglas base |
| BFF | POST | `/bff/reasignacion/ejecutar` | Ejecutar reasignacion |

Nota: tabla de endpoints principales usados por el frontend privado.

## Evidencia para defensa

- Frontend usa Component Pattern en vistas reutilizables.
- BFF centraliza la comunicacion con microservicios para evitar acoplar el frontend a multiples puertos.
- Microservicios usan Repository Pattern para persistencia.
- DTOs del BFF controlan los datos que viajan al cliente.
- Tests actualizados para validar la pantalla privada de acceso.
