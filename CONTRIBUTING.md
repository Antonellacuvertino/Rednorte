# Contribuir

1. Actualiza `main` y crea una rama `feature/<descripcion>` o `fix/<descripcion>`.
2. Realiza cambios pequenos y agrega pruebas del comportamiento modificado.
3. Ejecuta `ejecutar-pruebas.bat`; ningun componente puede bajar de 90%.
4. Publica la rama y abre un pull request contra `main`.
5. Espera CI, CodeQL, revision de dependencias y al menos una aprobacion.
6. Integra con squash merge y elimina la rama.

No se aceptan commits directos a `main`, secretos, bases H2, logs, `node_modules`
ni artefactos de compilacion.
