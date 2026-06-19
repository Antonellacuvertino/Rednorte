package com.rednorte.servicio_reasignacion.exception;

/**
 * Indica que la cita que se desea reasignar no existe.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
