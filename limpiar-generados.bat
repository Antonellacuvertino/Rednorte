@echo off
setlocal
cd /d "%~dp0"

echo Eliminando dependencias, compilados, reportes y bases locales...

if exist "frontend\node_modules" rmdir /s /q "frontend\node_modules"
if exist "frontend\dist" rmdir /s /q "frontend\dist"
if exist "frontend\coverage" rmdir /s /q "frontend\coverage"

for %%S in (bff-rednorte ms-pacientes ms-citas ms-lista-espera ms-reasignacion ms-auditoria ms-notificaciones) do (
  if exist "%%S\target" rmdir /s /q "%%S\target"
  if exist "%%S\data" rmdir /s /q "%%S\data"
)

echo Proyecto limpio. Ejecuta iniciar-servicios.bat para reinstalar y arrancar.
endlocal
