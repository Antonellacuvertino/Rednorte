@echo off
setlocal

echo === Frontend: Vitest y cobertura ===
pushd frontend
call npm.cmd run test:coverage
if errorlevel 1 exit /b 1
popd

for %%S in (bff-rednorte ms-pacientes ms-citas ms-lista-espera ms-reasignacion ms-auditoria ms-notificaciones) do (
  echo === %%S: JUnit y JaCoCo ===
  pushd %%S
  call mvnw.cmd test
  if errorlevel 1 exit /b 1
  popd
)

echo.
echo Todas las pruebas finalizaron correctamente.
endlocal
