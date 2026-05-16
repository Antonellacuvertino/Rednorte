@echo off
echo Iniciando servicios de RedNorte...

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

timeout /t 5 /nobreak > nul

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
echo - H2 Console: http://localhost:8081/h2-console (usuario: sa, password: password)
echo.
pause