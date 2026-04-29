package com.daniel_parra16.vitalNode.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String mensaje) {
        super(mensaje);
    }
}
