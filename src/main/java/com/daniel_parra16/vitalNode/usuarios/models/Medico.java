package com.daniel_parra16.vitalNode.usuarios.models;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Medicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medico {

    @Id
    private String id;

    @Indexed(unique = true)
    private String numeroDocumento; // 🔥 relación con Usuario

    private String registroMedico; // tarjeta profesional

    private String especialidad;

    private List<String> subEspecialidades;

    private Integer aniosExperiencia;

    private boolean disponible;

    private List<String> horarios;

    @CreatedDate
    private LocalDateTime createdAt;
}