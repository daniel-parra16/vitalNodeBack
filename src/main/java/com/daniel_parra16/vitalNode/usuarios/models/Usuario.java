package com.daniel_parra16.vitalNode.usuarios.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "UsuariosPerfil")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndex(name = "idx_documento_numero", def = "{'doc.numero': 1}", unique = true)
public class Usuario {

    @Id
    private String id;

    private Documento doc;

    private String nom;

    private String ape;

    @Indexed(unique = true, sparse = true)
    private String email;

    private String phone;

    private Direccion direc;

    private boolean activo;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
