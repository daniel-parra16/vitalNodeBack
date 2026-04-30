package com.daniel_parra16.vitalNode.usuarios.services;

import java.util.List;

import com.daniel_parra16.vitalNode.usuarios.dtos.CreateUserRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.UpdateRoleRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.UpdateUserRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.UserResponse;

public interface UsuarioService {

    UserResponse createUser(CreateUserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUser(String numeroDocumento);

    UserResponse updateUser(String numeroDocumento, UpdateUserRequest request);

    void updateRoles(String numeroDocumento, UpdateRoleRequest request);

    void deleteUser(String numeroDocumento);
}
