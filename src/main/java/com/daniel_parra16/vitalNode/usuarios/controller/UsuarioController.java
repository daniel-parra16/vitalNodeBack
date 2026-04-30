package com.daniel_parra16.vitalNode.usuarios.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.daniel_parra16.vitalNode.exceptions.ApiResponse;
import com.daniel_parra16.vitalNode.usuarios.dtos.user.CreateUserRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.user.UpdateRoleRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.user.UpdateUserRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.user.UserResponse;
import com.daniel_parra16.vitalNode.usuarios.services.usuario.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

        private final UsuarioService usuarioService;

        // 🔥 CREAR USUARIO (ADMIN)
        @PostMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<UserResponse>> createUser(
                        @Valid @RequestBody CreateUserRequest request) {

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.<UserResponse>builder()
                                                .status(201)
                                                .message("Usuario creado correctamente")
                                                .data(usuarioService.createUser(request))
                                                .build());
        }

        // 🔥 LISTAR (ADMIN)
        @GetMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {

                List<UserResponse> users = usuarioService.getAllUsers();

                return ResponseEntity.ok(
                                ApiResponse.<List<UserResponse>>builder()
                                                .status(200)
                                                .message("Lista de usuarios")
                                                .data(users)
                                                .build());
        }

        // 🔥 VER USUARIO (ADMIN o dueño)
        @GetMapping("/{numeroDocumento}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<UserResponse>> getUser(
                        @PathVariable String numeroDocumento) {

                return ResponseEntity.ok(
                                ApiResponse.<UserResponse>builder()
                                                .status(200)
                                                .message("Usuario encontrado")
                                                .data(usuarioService.getUser(numeroDocumento))
                                                .build());
        }

        // 🔥 ACTUALIZAR (ADMIN o dueño)
        @PutMapping("/{numeroDocumento}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<UserResponse>> updateUser(
                        @PathVariable String numeroDocumento,
                        @RequestBody UpdateUserRequest request) {

                return ResponseEntity.ok(
                                ApiResponse.<UserResponse>builder()
                                                .status(200)
                                                .message("Usuario actualizado")
                                                .data(usuarioService.updateUser(numeroDocumento, request))
                                                .build());
        }

        // 🔥 CAMBIAR ROLES (ADMIN)
        @PutMapping("/{numeroDocumento}/roles")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<Void>> updateRoles(
                        @PathVariable String numeroDocumento,
                        @RequestBody UpdateRoleRequest request) {

                usuarioService.updateRoles(numeroDocumento, request);

                return ResponseEntity.ok(
                                ApiResponse.<Void>builder()
                                                .status(200)
                                                .message("Roles actualizados")
                                                .build());
        }

        // 🔥 DESACTIVAR (ADMIN)
        @DeleteMapping("/{numeroDocumento}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<Void>> deleteUser(
                        @PathVariable String numeroDocumento) {

                usuarioService.deleteUser(numeroDocumento);

                return ResponseEntity.ok(
                                ApiResponse.<Void>builder()
                                                .status(200)
                                                .message("Usuario desactivado")
                                                .build());
        }
}