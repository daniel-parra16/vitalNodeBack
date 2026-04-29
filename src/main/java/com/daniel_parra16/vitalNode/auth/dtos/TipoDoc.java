package com.daniel_parra16.vitalNode.auth.dtos;

public enum TipoDoc {
    CC("Cédula de ciudadanía"),
    TI("Tarjeta de identidad"),
    NIT("NIT"),
    CE("Cédula de extranjería"),
    PASAPORTE("Pasaporte");

    private final String descripcion;

    TipoDoc(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
