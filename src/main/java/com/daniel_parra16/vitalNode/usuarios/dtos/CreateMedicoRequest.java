package com.daniel_parra16.vitalNode.usuarios.dtos;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateMedicoRequest {

    @NotBlank
    private String registroMedico;

    @NotBlank
    private String especialidad;

    private List<String> subEspecialidades;

    private Integer aniosExperiencia;
}