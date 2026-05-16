# Frontend Hospital Red Norte

Aplicacion React + Vite para gestion privada RedSalud. La primera pantalla es autenticacion: registro de medico con dominio `@redsalud.cl` e inicio de sesion local para entrar al panel.

## Componentes principales

- `Login.jsx`: registro e inicio de sesion privado.
- `PatientList.jsx`, `PatientDetail.jsx`, `PatientForm.jsx`: gestion de pacientes.
- `CitasPublic.jsx`: agenda interna de citas.
- `AppointmentForm.jsx`: creacion de citas.
- `MicroservicesPanel.jsx`: vista de Lista de espera y reglas de reasignacion.

## Ejecucion

```bash
npm install
npm run dev
```

Frontend: `http://localhost:5173`

## Conexion con BFF

Por defecto consume `http://localhost:8085/bff`. Se puede cambiar con:

```bash
VITE_API_BASE=http://localhost:8085/bff
```

## Pruebas

```bash
npm test
npm run build
```
