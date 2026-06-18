@echo off
setlocal
cd /d "%~dp0"
echo Iniciando servicios de RedNorte...

echo Iniciando Redis y RabbitMQ...
docker compose up -d
if errorlevel 1 (
  echo No se pudo iniciar Docker. Verifica que Docker Desktop este activo.
  exit /b 1
)

if not exist "frontend\node_modules" (
  echo Instalando dependencias frontend...
  pushd frontend
  call npm.cmd ci
  if errorlevel 1 exit /b 1
  popd
)

echo Iniciando ms-pacientes...
start "ms-pacientes" cmd /k "cd ms-pacientes && .\mvnw.cmd spring-boot:run"

timeout /t 5 /nobreak > nul

echo Iniciando ms-citas...
start "ms-citas" cmd /k "cd ms-citas && .\mvnw.cmd spring-boot:run"

timeout /t 5 /nobreak > nul

echo Iniciando ms-lista-espera...
start "ms-lista-espera" cmd /k "cd ms-lista-espera && .\mvnw.cmd spring-boot:run"

timeout /t 5 /nobreak > nul

echo Iniciando ms-reasignacion...
start "ms-reasignacion" cmd /k "cd ms-reasignacion && .\mvnw.cmd spring-boot:run"

echo Iniciando ms-auditoria...
start "ms-auditoria" cmd /k "cd ms-auditoria && .\mvnw.cmd spring-boot:run"

echo Iniciando ms-notificaciones...
start "ms-notificaciones" cmd /k "cd ms-notificaciones && .\mvnw.cmd spring-boot:run"

timeout /t 8 /nobreak > nul

echo Iniciando bff-rednorte...
start "bff-rednorte" cmd /k "cd bff-rednorte && .\mvnw.cmd spring-boot:run"

timeout /t 5 /nobreak > nul

echo Iniciando frontend...
start "frontend" cmd /k "cd frontend && npm run dev"

echo Todos los servicios han sido iniciados.
echo.
echo URLs de acceso:
echo - Frontend: http://localhost:5173
echo - BFF: http://localhost:8085
echo - MS Pacientes: http://localhost:8081
echo - MS Citas: http://localhost:8082
echo - MS Lista Espera: http://localhost:8083
echo - MS Reasignacion: http://localhost:8084
echo - MS Auditoria: http://localhost:8086
echo - MS Notificaciones: http://localhost:8087
echo - RabbitMQ: http://localhost:15672 (guest / guest)
echo.
pause
endlocal
