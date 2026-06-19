package cl.duoc.rednorte.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthUserServiceTest {

    @Test
    void registraConHashYAutentica() {
        AuthUserService service = new AuthUserService(
                new BCryptPasswordEncoder(),
                "medico@redsalud.cl",
                "salud1234",
                "Medico Demo");

        AuthUserService.AuthUser user = service.register(
                "Dra. Ana",
                "ANA@REDSALUD.CL",
                "segura123");

        assertEquals("ana@redsalud.cl", user.email());
        assertNotEquals("segura123", user.passwordHash());
        assertEquals(user, service.authenticate("ana@redsalud.cl", "segura123"));
        assertEquals(2, service.count());
    }

    @Test
    void rechazaDominioPasswordYDuplicadosInvalidos() {
        AuthUserService service = new AuthUserService(
                new BCryptPasswordEncoder(),
                "medico@redsalud.cl",
                "salud1234",
                "Medico Demo");

        assertThrows(IllegalArgumentException.class,
                () -> service.register("Externo", "externo@gmail.com", "segura123"));
        assertThrows(IllegalArgumentException.class,
                () -> service.register("Dra. Ana", "ana@redsalud.cl", "corta"));
        assertThrows(IllegalArgumentException.class,
                () -> service.authenticate("medico@redsalud.cl", "incorrecta"));
        assertThrows(IllegalArgumentException.class,
                () -> service.register("Duplicado", "medico@redsalud.cl", "otra1234"));
        assertThrows(IllegalArgumentException.class,
                () -> service.register(null, "nuevo@redsalud.cl", "segura123"));
        assertThrows(IllegalArgumentException.class,
                () -> service.register("Sin correo", null, "segura123"));
        assertThrows(IllegalArgumentException.class,
                () -> service.authenticate(null, null));
    }
}
