package com.daniel_parra16.vitalNode.usuarios.dtos.user;

import java.util.List;

import com.daniel_parra16.vitalNode.auth.models.Rol;
import com.daniel_parra16.vitalNode.usuarios.models.TipoDoc;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank
    private String numeroDocumento;

    @NotNull
    private TipoDoc tipo;

    @NotBlank
    private String nom;

    @NotBlank
    private String ape;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String password;

    @NotEmpty
    private List<Rol> roles;
}