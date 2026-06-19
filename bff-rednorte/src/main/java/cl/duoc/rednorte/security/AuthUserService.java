package cl.duoc.rednorte.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestiona el registro y autenticacion de usuarios institucionales.
 */
@Service
public class AuthUserService {

    private static final String EMAIL_DOMAIN = "@redsalud.cl";

    private final PasswordEncoder passwordEncoder;
    private final Map<String, AuthUser> users = new ConcurrentHashMap<>();

    public AuthUserService(
            PasswordEncoder passwordEncoder,
            @Value("${security.demo-user.email:medico@redsalud.cl}") String demoEmail,
            @Value("${security.demo-user.password:salud123}") String demoPassword,
            @Value("${security.demo-user.name:Medico RedNorte}") String demoName) {
        this.passwordEncoder = passwordEncoder;
        register(demoName, demoEmail, demoPassword);
    }

    /**
     * Registra un usuario con contrasena BCrypt.
     *
     * @return usuario institucional creado
     */
    public AuthUser register(String name, String email, String rawPassword) {
        String normalizedEmail = normalizeEmail(email);
        validate(name, normalizedEmail, rawPassword);

        AuthUser user = new AuthUser(name.trim(), normalizedEmail, passwordEncoder.encode(rawPassword));
        AuthUser existing = users.putIfAbsent(normalizedEmail, user);
        if (existing != null) {
            throw new IllegalArgumentException("El correo ya esta registrado");
        }
        return user;
    }

    /**
     * Valida correo y contrasena contra el hash almacenado.
     *
     * @return usuario autenticado
     */
    public AuthUser authenticate(String email, String rawPassword) {
        AuthUser user = users.get(normalizeEmail(email));
        if (user == null || rawPassword == null || !passwordEncoder.matches(rawPassword, user.passwordHash())) {
            throw new IllegalArgumentException("Credenciales institucionales invalidas");
        }
        return user;
    }

    public int count() {
        return users.size();
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private void validate(String name, String email, String password) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (!email.endsWith(EMAIL_DOMAIN)) {
            throw new IllegalArgumentException("Solo se permiten correos @redsalud.cl");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("La contrasena debe tener al menos 8 caracteres");
        }
    }

    public record AuthUser(String name, String email, String passwordHash) {
    }
}
