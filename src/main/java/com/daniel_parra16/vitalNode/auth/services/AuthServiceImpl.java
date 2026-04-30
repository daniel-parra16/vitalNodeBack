package com.daniel_parra16.vitalNode.auth.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.daniel_parra16.vitalNode.auth.dtos.LoginRequest;
import com.daniel_parra16.vitalNode.auth.dtos.LoginResponse;
import com.daniel_parra16.vitalNode.auth.dtos.RefreshTokenRequest;
import com.daniel_parra16.vitalNode.auth.dtos.RegistroRequest;
import com.daniel_parra16.vitalNode.auth.models.Rol;
import com.daniel_parra16.vitalNode.auth.models.Sesion;
import com.daniel_parra16.vitalNode.auth.models.UsuarioAuth;
import com.daniel_parra16.vitalNode.auth.repositories.SesionRepository;
import com.daniel_parra16.vitalNode.auth.repositories.UsuarioAuthRepository;
import com.daniel_parra16.vitalNode.exceptions.BadRequestException;
import com.daniel_parra16.vitalNode.exceptions.ConflictException;
import com.daniel_parra16.vitalNode.exceptions.NotFoundException;
import com.daniel_parra16.vitalNode.exceptions.UnauthorizedException;
import com.daniel_parra16.vitalNode.security.JwtService;
import com.daniel_parra16.vitalNode.usuarios.models.TipoDoc;
import com.daniel_parra16.vitalNode.usuarios.models.Usuario;
import com.daniel_parra16.vitalNode.usuarios.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

        private final UsuarioRepository usuarioRepository;
        private final UsuarioAuthRepository usuarioAuthRepository;
        private final SesionRepository sesionRepository;
        private final JwtService jwtService;
        private final PasswordEncoder passwordEncoder;

        // ─────────────────────────────────────────
        // REGISTRO
        // ─────────────────────────────────────────

        @Override

        public Map<String, String> registro(RegistroRequest request) {

                // 1. Verificar que el correo no esté registrado
                if (usuarioRepository.existsByEmail(request.getCorreo()))
                        throw new ConflictException("El correo ya está registrado");

                // 2. Verificar que el número de documento no esté registrado
                if (usuarioAuthRepository.existsByNumeroDocumento(request.getNumeroDocumento()))
                        throw new ConflictException("El número de documento ya está registrado");

                // 3. Generar UUID compartido para Usuario y UsuarioAuth
                String uuid = UUID.randomUUID().toString();

                // 4. Crear y guardar Usuario con datos personales
                Usuario usuario = Usuario.builder()
                                .id(uuid)
                                .tipo(TipoDoc.valueOf(request.getTipoDocumento().name()))
                                .numeroDocumento(request.getNumeroDocumento())
                                .nom(request.getNombres())
                                .ape(request.getApellidos())
                                .phone(request.getTelefono())
                                .email(request.getCorreo())
                                .activo(true)
                                .build();

                usuarioRepository.save(usuario);

                // 5. Crear y guardar UsuarioAuth con credenciales
                UsuarioAuth usuarioAuth = UsuarioAuth.builder()
                                .id(uuid)
                                .numeroDocumento(request.getNumeroDocumento())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .roles(List.of(Rol.ROLE_USER))
                                .activo(true)
                                .build();

                usuarioAuthRepository.save(usuarioAuth);

                return Map.of("mensaje", "Usuario registrado correctamente.");
        }

        // ─────────────────────────────────────────
        // LOGIN
        // ─────────────────────────────────────────

        @Override

        public LoginResponse login(LoginRequest request) {

                // 1. Buscar credenciales por número de documento
                UsuarioAuth usuarioAuth = usuarioAuthRepository
                                .findByNumeroDocumento(request.getNumeroDocumento())
                                .orElseThrow(() -> new UnauthorizedException(
                                                "Número de documento o contraseña incorrectos"));

                // 2. Verificar contraseña
                if (!passwordEncoder.matches(request.getPassword(), usuarioAuth.getPassword()))
                        throw new UnauthorizedException("Número de documento o contraseña incorrectos");

                // 3. Buscar datos personales del usuario
                System.out.println("BUSCANDO USUARIO: " + usuarioAuth);
                System.out.println("BUSCANDO USUARIO EN BD: " + usuarioAuth.getId());
                Usuario usuario = usuarioRepository.findById(usuarioAuth.getId())
                                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

                // 4. Verificar que el usuario esté activo
                if (!usuario.isActivo())
                        throw new BadRequestException("Tu cuenta está desactivada");

                // 5. Cerrar sesión anterior si existe
                List<Sesion> sesionesActivas = sesionRepository.findByUsuarioIdAndActivaTrue(usuarioAuth.getId());

                for (Sesion sesion : sesionesActivas) {
                        sesion.setActiva(false);
                }

                sesionRepository.saveAll(sesionesActivas);

                // 6. Generar tokens
                List<String> roles = usuarioAuth.getRoles()
                                .stream()
                                .map(Rol::name)
                                .toList();

                String accessToken = jwtService.generarAccessToken(usuarioAuth.getId(), roles,
                                usuario.getNom() + " " + usuario.getApe(),
                                usuario.getNumeroDocumento());
                String refreshToken = jwtService.generarRefreshToken(usuarioAuth.getId());

                // 7. Guardar nueva sesión
                guardarSesion(usuarioAuth.getId(), refreshToken);

                // 8. Devolver respuesta
                return LoginResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .tipo("Bearer")
                                .build();
        }

        // ─────────────────────────────────────────
        // REFRESH TOKEN
        // ─────────────────────────────────────────

        @Override
        public LoginResponse renovarToken(RefreshTokenRequest request) {

                String refreshToken = request.getRefreshToken();

                // 1. Validar que sea un Refresh Token válido y no expirado
                if (!jwtService.esRefreshTokenValido(refreshToken))
                        throw new UnauthorizedException("Refresh token inválido o expirado");

                // 2. Buscar la sesión en BD
                Sesion sesion = sesionRepository.findByRefreshToken(refreshToken)
                                .orElseThrow(() -> new UnauthorizedException("Sesión no encontrada"));

                // 3. Verificar que la sesión esté activa
                if (!sesion.isActiva())
                        throw new UnauthorizedException("La sesión fue cerrada");

                // 4. Verificar inactividad
                long minutosInactivo = ChronoUnit.MINUTES.between(
                                sesion.getUltimaActividad(),
                                LocalDateTime.now());

                if (minutosInactivo > 30) {
                        sesion.setActiva(false);
                        sesionRepository.save(sesion);
                        throw new UnauthorizedException("Sesión expirada por inactividad");
                }

                // 5. Actualizar última actividad
                sesion.setUltimaActividad(LocalDateTime.now());
                sesionRepository.save(sesion);

                // 6. Obtener datos del usuario
                String usuarioId = jwtService.extraerUsuarioId(refreshToken);

                UsuarioAuth usuarioAuth = usuarioAuthRepository.findById(usuarioId)
                                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

                Usuario usuario = usuarioRepository.findById(usuarioId)
                                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

                List<String> roles = usuarioAuth.getRoles()
                                .stream()
                                .map(Rol::name)
                                .toList();

                // 7. Generar nuevo Access Token
                String nuevoAccessToken = jwtService.generarAccessToken(usuarioId, roles, usuario.getNom(),
                                usuario.getNumeroDocumento());

                return LoginResponse.builder()
                                .accessToken(nuevoAccessToken)
                                .refreshToken(refreshToken)
                                .tipo("Bearer")
                                .build();
        }

        // ─────────────────────────────────────────
        // CERRAR SESIÓN
        // ─────────────────────────────────────────

        @Override
        public void logout(RefreshTokenRequest request) {
                sesionRepository.findByRefreshToken(request.getRefreshToken())
                                .ifPresent(sesion -> {
                                        sesion.setActiva(false);
                                        sesionRepository.save(sesion);
                                });
        }

        // ─────────────────────────────────────────
        // MÉTODOS PRIVADOS
        // ─────────────────────────────────────────

        private void guardarSesion(String usuarioId, String refreshToken) {
                Sesion sesion = Sesion.builder()
                                .usuarioId(usuarioId)
                                .refreshToken(refreshToken)
                                .ultimaActividad(LocalDateTime.now())
                                .expiraEn(LocalDateTime.now().plusDays(7))
                                .activa(true)
                                .build();

                sesionRepository.save(sesion);
        }

}