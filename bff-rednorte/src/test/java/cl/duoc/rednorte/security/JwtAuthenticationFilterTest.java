package cl.duoc.rednorte.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtAuthenticationFilterTest {

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void autenticaBearerValido() throws Exception {
        JwtService jwtService = mock(JwtService.class);
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("medico@redsalud.cl");
        when(claims.get("role", String.class)).thenReturn("MEDICO");
        when(jwtService.validate("valido")).thenReturn(claims);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valido");
        FilterChain chain = mock(FilterChain.class);

        filter.doFilterInternal(request, new MockHttpServletResponse(), chain);

        assertEquals("medico@redsalud.cl", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(chain).doFilter(
                org.mockito.ArgumentMatchers.eq(request),
                org.mockito.ArgumentMatchers.any());
    }

    @Test
    void ignoraAusenciaDeBearerYTokenInvalido() throws Exception {
        JwtService jwtService = mock(JwtService.class);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);
        FilterChain chain = mock(FilterChain.class);
        MockHttpServletRequest request = new MockHttpServletRequest();

        filter.doFilterInternal(request, new MockHttpServletResponse(), chain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        request.addHeader("Authorization", "Bearer alterado");
        when(jwtService.validate("alterado")).thenThrow(new IllegalArgumentException("firma invalida"));
        filter.doFilterInternal(request, new MockHttpServletResponse(), chain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void rechazaTokenSinRol() throws Exception {
        JwtService jwtService = mock(JwtService.class);
        Claims claims = mock(Claims.class);
        when(jwtService.validate("sin-rol")).thenReturn(claims);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer sin-rol");

        filter.doFilterInternal(request, new MockHttpServletResponse(), mock(FilterChain.class));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
