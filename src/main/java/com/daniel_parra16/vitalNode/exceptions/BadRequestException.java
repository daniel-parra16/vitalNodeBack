package com.daniel_parra16.vitalNode.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String mensaje) {
        super(mensaje);
    }
}
