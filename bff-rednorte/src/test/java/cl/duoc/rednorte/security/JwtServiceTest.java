package cl.duoc.rednorte.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

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
        assertEquals(7200, service.expirationSeconds());
    }

    @Test
    void rechazaTokenAlterado() {
        JwtService service = new JwtService(SECRET, 120);
        String token = service.generateToken("medico@redsalud.cl", "Dra. Norte");

        assertThrows(RuntimeException.class, () -> service.validate(token + "alterado"));
    }

    @Test
    void tokenEsCompatibleConLosResourceServers() {
        JwtService service = new JwtService(SECRET, 120);
        var key = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        var decoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();

        var jwt = decoder.decode(service.generateToken("medico@redsalud.cl", "Dra. Norte"));

        assertEquals("medico@redsalud.cl", jwt.getSubject());
        assertEquals("MEDICO", jwt.getClaimAsString("role"));
    }
}
