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

@Document(collection = "Enfermeras")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enfermera {

    @Id
    private String id;

    @Indexed(unique = true)
    private String numeroDocumento;

    private String registroProfesional;

    private String area;

    private List<String> habilidades;

    private boolean activo;

    @CreatedDate
    private LocalDateTime createdAt;
}