# Frontend Hospital Red Norte

Aplicacion React + Vite para la gestion privada de pacientes, citas, lista de
espera, reasignacion, auditoria y notificaciones.

## Instalacion y ejecucion

```bat
npm install
npm run dev
```

El frontend se abre en `http://localhost:5173` y consume por defecto el BFF en
`http://localhost:8085/bff`.

Para usar otra direccion:

```text
VITE_API_BASE=http://localhost:8085/bff
```

La autenticacion se realiza contra `/auth/login` y `/auth/register`. El token
Bearer se adjunta a cada solicitud protegida; las claves no se guardan en el
navegador.

## Pruebas

```bat
npm test
npm run test:coverage
npm run build
```

El reporte Vitest se genera en `coverage/index.html`.
