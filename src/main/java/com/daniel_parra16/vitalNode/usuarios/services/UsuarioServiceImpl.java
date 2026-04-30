package com.daniel_parra16.vitalNode.usuarios.services;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.daniel_parra16.vitalNode.auth.models.Rol;
import com.daniel_parra16.vitalNode.auth.models.UsuarioAuth;
import com.daniel_parra16.vitalNode.auth.repositories.UsuarioAuthRepository;
import com.daniel_parra16.vitalNode.exceptions.BadRequestException;
import com.daniel_parra16.vitalNode.usuarios.dtos.CreateUserRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.UpdateRoleRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.UpdateUserRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.UserResponse;
import com.daniel_parra16.vitalNode.usuarios.models.Usuario;
import com.daniel_parra16.vitalNode.usuarios.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioAuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        if (usuarioRepository.existsByNumeroDocumento(request.getNumeroDocumento())) {
            throw new RuntimeException("El usuario ya existe");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está en uso");
        }

        Usuario usuario = Usuario.builder()
                .numeroDocumento(request.getNumeroDocumento())
                .tipo(request.getTipo())
                .nom(request.getNom())
                .ape(request.getApe())
                .email(request.getEmail())
                .phone(request.getPhone())
                .activo(true)
                .build();

        usuarioRepository.save(usuario);

        UsuarioAuth auth = UsuarioAuth.builder()
                .numeroDocumento(request.getNumeroDocumento())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(request.getRoles())
                .activo(true)
                .build();

        authRepository.save(auth);

        return mapToResponse(usuario, auth);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return usuarioRepository.findAll().stream()
                .map(user -> {
                    UsuarioAuth auth = authRepository
                            .findByNumeroDocumento(user.getNumeroDocumento())
                            .orElseThrow();
                    return mapToResponse(user, auth);
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUser(String numeroDocumento) {

        Usuario user = usuarioRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UsuarioAuth auth = authRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new RuntimeException("Auth no encontrado"));

        return mapToResponse(user, auth);
    }

    @Override
    public UserResponse updateUser(String numeroDocumento, UpdateUserRequest request) {

        Usuario user = usuarioRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getNom() != null)
            user.setNom(request.getNom());
        if (request.getApe() != null)
            user.setApe(request.getApe());
        if (request.getEmail() != null)
            user.setEmail(request.getEmail());
        if (request.getPhone() != null)
            user.setPhone(request.getPhone());

        usuarioRepository.save(user);

        UsuarioAuth auth = authRepository.findByNumeroDocumento(numeroDocumento).orElseThrow();

        return mapToResponse(user, auth);
    }

    @Override
    public void updateRoles(String numeroDocumento, UpdateRoleRequest request) {

        // 🔥 Validar roles válidos
        if (!EnumSet.allOf(Rol.class).containsAll(request.getRoles())) {
            throw new BadRequestException("Uno o más roles son inválidos");
        }

        // 🔥 Obtener usuario auth
        UsuarioAuth auth = authRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new RuntimeException("Auth no encontrado"));

        boolean eraAdmin = auth.getRoles().contains(Rol.ROLE_ADMIN);
        boolean seraAdmin = request.getRoles().contains(Rol.ROLE_ADMIN);

        // 🔥 Validar último admin SOLO si lo estás quitando
        if (eraAdmin && !seraAdmin) {

            long totalAdmins = authRepository.countByRolesContaining(Rol.ROLE_ADMIN);

            if (totalAdmins <= 1) {
                throw new BadRequestException("No puedes eliminar el último administrador");
            }
        }

        // 🔥 Actualizar roles
        auth.setRoles(request.getRoles());
        authRepository.save(auth);
    }

    @Override
    public void deleteUser(String numeroDocumento) {

        Usuario user = usuarioRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UsuarioAuth auth = authRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new RuntimeException("Auth no encontrado"));

        // 🔥 Validar último admin
        if (auth.getRoles().contains(Rol.ROLE_ADMIN)) {

            long totalAdmins = authRepository.countByRolesContaining(Rol.ROLE_ADMIN);

            if (totalAdmins <= 1) {
                throw new BadRequestException("No puedes eliminar el último administrador");
            }
        }

        // 🔥 Soft delete
        user.setActivo(false);
        usuarioRepository.save(user);

        auth.setActivo(false);
        authRepository.save(auth);
    }

    private UserResponse mapToResponse(Usuario user, UsuarioAuth auth) {
        return UserResponse.builder()
                .numeroDocumento(user.getNumeroDocumento())
                .tipo(user.getTipo())
                .nom(user.getNom())
                .ape(user.getApe())
                .email(user.getEmail())
                .phone(user.getPhone())
                .activo(user.isActivo())
                .roles(auth.getRoles())
                .build();
    }
}