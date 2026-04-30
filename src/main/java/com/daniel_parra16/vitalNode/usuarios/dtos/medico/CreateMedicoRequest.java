package com.daniel_parra16.vitalNode.usuarios.dtos.medico;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMedicoRequest {

    @NotBlank(message = "El registro médico es obligatorio")
    private String registroMedico;

    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;

    private List<String> subEspecialidades;

    @NotNull(message = "Los años de experiencia son obligatorios")
    @Min(value = 0, message = "No puede ser negativo")
    private Integer aniosExperiencia;
}