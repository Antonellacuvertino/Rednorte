package cl.duoc.rednorte.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTest {

    private static final String SECRET = "clave-de-pruebas-jwt-rednorte-con-32-caracteres-minimo";

    @Test
    void generaYValidaTokenFirmado() {
        JwtService service = new JwtService(SECRET, 120);

        String token = service.generateToken("medico@redsalud.cl", "Dra. Norte");
        Claims claims = service.validate(token);

        assertEquals("medico@redsalud.cl", claims.getSubject());
        assertEquals("Dra. Norte", claims.get("name"));
        assertEquals("MEDICO", claims.get("role"));
    }

    @Test
    void rechazaTokenAlterado() {
        JwtService service = new JwtService(SECRET, 120);
        String token = service.generateToken("medico@redsalud.cl", "Dra. Norte");

        assertThrows(RuntimeException.class, () -> service.validate(token + "alterado"));
    }
}
