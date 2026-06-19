package com.rednorte.servicio_auditoria.exception;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    @Test
    void traduceExcepcionesARespuestasHttp() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/auditoria/99");

        assertEquals(404, handler.handleNotFound(
                new ResourceNotFoundException("No existe"), request).getStatusCode().value());
        assertEquals(400, handler.handleBadRequest(
                new IllegalArgumentException("Dato invalido"), request).getStatusCode().value());
        assertEquals(500, handler.handleUnexpected(
                new RuntimeException("Falla"), request).getStatusCode().value());
    }
}
