package com.daniel_parra16.vitalNode.usuarios.dtos.enfermera;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateEnfermeraRequest {

    @NotBlank(message = "El registro profesional es obligatorio")
    private String registroProfesional;

    @NotBlank(message = "El área es obligatoria")
    private String area;

    private List<String> habilidades;
}