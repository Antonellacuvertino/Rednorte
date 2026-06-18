package cl.duoc.rednorte.controller;

import cl.duoc.rednorte.security.AuthUserService;
import cl.duoc.rednorte.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthControllerTest {

    private AuthController controller;

    @BeforeEach
    void setUp() {
        AuthUserService users = new AuthUserService(
                new BCryptPasswordEncoder(),
                "medico@redsalud.cl",
                "salud1234",
                "Medico Demo");
        JwtService jwtService = new JwtService(
                "clave-de-pruebas-jwt-rednorte-con-32-caracteres-minimo",
                120);
        controller = new AuthController(jwtService, users);
    }

    @Test
    void registraYAutenticaUsuario() {
        var register = controller.register(new AuthController.AuthRequest(
                "ana@redsalud.cl",
                "segura123",
                "Dra. Ana"));

        assertEquals(201, register.getStatusCode().value());
        assertNotNull(register.getBody().token());

        var login = controller.login(new AuthController.AuthRequest(
                "ana@redsalud.cl",
                "segura123",
                null));

        assertEquals(200, login.getStatusCode().value());
        assertEquals("ana@redsalud.cl", login.getBody().email());
    }

    @Test
    void rechazaCredencialesInvalidas() {
        assertThrows(ResponseStatusException.class, () -> controller.login(
                new AuthController.AuthRequest("medico@redsalud.cl", "incorrecta", null)));
        assertThrows(ResponseStatusException.class, () -> controller.register(
                new AuthController.AuthRequest("externo@gmail.com", "segura123", "Externo")));
    }
}
