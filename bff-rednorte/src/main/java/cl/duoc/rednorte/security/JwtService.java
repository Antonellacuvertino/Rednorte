package cl.duoc.rednorte.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * Genera y valida tokens JWT firmados con HMAC SHA-256.
 */
@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMinutes;

    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.expiration-minutes:120}") long expirationMinutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    /**
     * Genera un token con identidad, rol y fecha de expiracion.
     *
     * @param email correo institucional usado como sujeto
     * @param name nombre visible del usuario
     * @return token JWT compacto y firmado
     */
    public String generateToken(String email, String name) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(email)
                .claims(Map.of("name", name, "role", "MEDICO"))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expirationMinutes * 60)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Claims validate(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * @return vigencia configurada del token en segundos
     */
    public long expirationSeconds() {
        return expirationMinutes * 60;
    }
}
