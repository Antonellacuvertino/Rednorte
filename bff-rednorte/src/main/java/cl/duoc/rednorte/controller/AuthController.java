package cl.duoc.rednorte.controller;

import cl.duoc.rednorte.security.AuthUserService;
import cl.duoc.rednorte.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final JwtService jwtService;
    private final AuthUserService authUserService;

    public AuthController(JwtService jwtService, AuthUserService authUserService) {
        this.jwtService = jwtService;
        this.authUserService = authUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            AuthUserService.AuthUser user = authUserService.authenticate(request.email(), request.password());
            return ResponseEntity.ok(createResponse(user));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales institucionales invalidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        try {
            AuthUserService.AuthUser user = authUserService.register(request.name(), request.email(), request.password());
            return ResponseEntity.status(HttpStatus.CREATED).body(createResponse(user));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    private AuthResponse createResponse(AuthUserService.AuthUser user) {
        String token = jwtService.generateToken(user.email(), user.name());
        return new AuthResponse(
                token,
                "Bearer",
                7200,
                user.email(),
                user.name(),
                "medico",
                authUserService.count());
    }

    public record AuthRequest(String email, String password, String name) {
    }

    public record AuthResponse(
            String token,
            String tokenType,
            long expiresIn,
            String email,
            String name,
            String role,
            int registeredUsers) {
    }
}
