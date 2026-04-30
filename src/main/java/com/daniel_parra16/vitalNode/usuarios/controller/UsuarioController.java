package com.daniel_parra16.vitalNode.usuarios.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.daniel_parra16.vitalNode.usuarios.dtos.*;
import com.daniel_parra16.vitalNode.usuarios.services.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // 🔥 CREAR USUARIO (ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        return ResponseEntity.ok(usuarioService.createUser(request));
    }

    // 🔥 LISTAR (ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(usuarioService.getAllUsers());
    }

    // 🔥 VER USUARIO (ADMIN o dueño)
    @GetMapping("/{numeroDocumento}")
    @PreAuthorize("hasRole('ADMIN') or #numeroDocumento == authentication.name")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable String numeroDocumento) {

        return ResponseEntity.ok(usuarioService.getUser(numeroDocumento));
    }

    // 🔥 ACTUALIZAR (ADMIN o dueño)
    @PutMapping("/{numeroDocumento}")
    @PreAuthorize("hasRole('ADMIN') or #numeroDocumento == authentication.name")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String numeroDocumento,
            @RequestBody UpdateUserRequest request) {

        return ResponseEntity.ok(
                usuarioService.updateUser(numeroDocumento, request));
    }

    // 🔥 CAMBIAR ROLES (ADMIN)
    @PutMapping("/{numeroDocumento}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateRoles(
            @PathVariable String numeroDocumento,
            @RequestBody UpdateRoleRequest request) {

        usuarioService.updateRoles(numeroDocumento, request);
        return ResponseEntity.ok().build();
    }

    // 🔥 DESACTIVAR (ADMIN)
    @DeleteMapping("/{numeroDocumento}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String numeroDocumento) {

        usuarioService.deleteUser(numeroDocumento);
        return ResponseEntity.ok().build();
    }
}