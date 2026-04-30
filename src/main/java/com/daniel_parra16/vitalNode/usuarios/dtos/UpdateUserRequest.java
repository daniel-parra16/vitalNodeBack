package com.daniel_parra16.vitalNode.usuarios.dtos;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String nom;
    private String ape;
    private String email;
    private String phone;
}