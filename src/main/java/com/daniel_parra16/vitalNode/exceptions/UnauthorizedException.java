package com.daniel_parra16.vitalNode.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String mensaje) {
        super(mensaje);
    }

}
