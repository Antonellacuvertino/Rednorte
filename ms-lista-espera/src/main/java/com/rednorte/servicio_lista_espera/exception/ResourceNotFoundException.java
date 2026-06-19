package com.rednorte.servicio_lista_espera.exception;

/**
 * Indica que un registro de lista de espera no existe.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
