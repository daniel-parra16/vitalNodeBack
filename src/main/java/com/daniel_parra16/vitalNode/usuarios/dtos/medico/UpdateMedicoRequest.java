package com.daniel_parra16.vitalNode.usuarios.dtos.medico;

import java.util.List;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateMedicoRequest {

    private String registroMedico;

    private String especialidad;

    private List<String> subEspecialidades;

    @Min(value = 0, message = "No puede ser negativo")
    private Integer aniosExperiencia;

    private Boolean activo;
}