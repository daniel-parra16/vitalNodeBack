package com.daniel_parra16.vitalNode.usuarios.services.usuario;

import java.util.List;

import com.daniel_parra16.vitalNode.usuarios.dtos.user.CreateUserRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.user.UpdateRoleRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.user.UpdateUserRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.user.UserResponse;

public interface UsuarioService {

    UserResponse createUser(CreateUserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUser(String numeroDocumento);

    UserResponse updateUser(String numeroDocumento, UpdateUserRequest request);

    void updateRoles(String numeroDocumento, UpdateRoleRequest request);

    void deleteUser(String numeroDocumento);
}
