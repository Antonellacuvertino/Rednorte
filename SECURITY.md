# Politica de seguridad

No publiques vulnerabilidades, tokens, contrasenas ni datos de pacientes en un
issue publico. Reporta el hallazgo de forma privada al responsable del
repositorio e incluye componente, impacto y pasos de reproduccion.

Las credenciales de desarrollo son solo demostrativas. En despliegues reales se
deben definir `JWT_SECRET`, usuarios de RabbitMQ y claves de infraestructura
mediante secretos del entorno.

Las correcciones de seguridad se desarrollan en una rama `fix/security-*`, se
validan con CI y CodeQL, y se integran mediante pull request revisado.
