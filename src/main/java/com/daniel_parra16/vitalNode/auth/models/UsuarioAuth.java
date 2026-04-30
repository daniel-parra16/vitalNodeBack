package com.daniel_parra16.vitalNode.auth.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "UsuariosAuth")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UsuarioAuth {

    @Id
    private String id;

    @Indexed(unique = true)
    private String numeroDocumento;

    private String password;
    private List<Rol> roles;

    private boolean activo;

}
