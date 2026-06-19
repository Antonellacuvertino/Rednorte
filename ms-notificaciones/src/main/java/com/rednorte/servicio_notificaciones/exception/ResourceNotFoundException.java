package com.rednorte.servicio_notificaciones.exception;

/**
 * Indica que una notificacion solicitada no existe.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
