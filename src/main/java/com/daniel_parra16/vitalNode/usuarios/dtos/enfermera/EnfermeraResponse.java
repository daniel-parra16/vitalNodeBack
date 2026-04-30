package com.daniel_parra16.vitalNode.usuarios.dtos.enfermera;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnfermeraResponse {

    private String numeroDocumento;
    private String registroProfesional;
    private String area;
    private List<String> habilidades;
    private boolean activo;
}