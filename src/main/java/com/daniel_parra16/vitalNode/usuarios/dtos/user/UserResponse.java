package com.daniel_parra16.vitalNode.usuarios.dtos.user;

import java.util.List;

import com.daniel_parra16.vitalNode.auth.models.Rol;
import com.daniel_parra16.vitalNode.usuarios.models.TipoDoc;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private String numeroDocumento;
    private TipoDoc tipo;
    private String nom;
    private String ape;
    private String email;
    private String phone;
    private boolean activo;
    private List<Rol> roles;
}