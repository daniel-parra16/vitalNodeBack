package com.daniel_parra16.vitalNode.usuarios.dtos.user;

import java.util.List;

import com.daniel_parra16.vitalNode.auth.models.Rol;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateRoleRequest {

    @NotEmpty
    private List<Rol> roles;
}