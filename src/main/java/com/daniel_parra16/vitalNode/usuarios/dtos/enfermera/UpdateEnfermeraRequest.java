package com.daniel_parra16.vitalNode.usuarios.dtos.enfermera;

import java.util.List;

import lombok.Data;

@Data
public class UpdateEnfermeraRequest {

    private String registroProfesional;

    private String area;

    private List<String> habilidades;

    private Boolean activo;
}