package com.daniel_parra16.vitalNode.usuarios.dtos.medico;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MedicoResponse {

    private String numeroDocumento;
    private String registroMedico;
    private String especialidad;
    private List<String> subEspecialidades;
    private Integer aniosExperiencia;
    private boolean activo;
}