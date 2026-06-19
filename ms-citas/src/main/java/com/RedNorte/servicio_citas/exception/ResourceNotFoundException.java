package com.RedNorte.servicio_citas.exception;

/**
 * Indica que una cita solicitada no existe.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
