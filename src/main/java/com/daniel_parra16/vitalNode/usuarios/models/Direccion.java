package com.daniel_parra16.vitalNode.usuarios.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Direccion {

    private String calle;
    private String carrera;
    private String ciudad;
    private String pais;
    private String codigoPostal;
}