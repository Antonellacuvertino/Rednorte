package com.rednorte.servicio_auditoria.exception;

/**
 * Indica que un evento de auditoria no existe.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
