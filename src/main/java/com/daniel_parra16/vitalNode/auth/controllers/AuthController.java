package com.daniel_parra16.vitalNode.auth.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daniel_parra16.vitalNode.auth.dtos.LoginRequest;
import com.daniel_parra16.vitalNode.auth.dtos.LoginResponse;
import com.daniel_parra16.vitalNode.auth.dtos.RefreshTokenRequest;
import com.daniel_parra16.vitalNode.auth.dtos.RegistroRequest;
import com.daniel_parra16.vitalNode.auth.dtos.TipoDoc;
import com.daniel_parra16.vitalNode.auth.services.AuthService;
import com.daniel_parra16.vitalNode.exceptions.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/tipoDocumento")
    public ResponseEntity<List<Map<String, String>>> getTiposDocumento() {

        List<Map<String, String>> lista = Arrays.stream(TipoDoc.values())
                .map(tipo -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("value", tipo.name()); // CC
                    map.put("label", tipo.getDescripcion()); // Cédula de ciudadanía
                    return map;
                })
                .toList();

        return ResponseEntity.ok(lista);
    }

    // POST /api/auth/registro
    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<Map<String, String>>> registro(
            @Valid @RequestBody RegistroRequest request) {

        Map<String, String> response = authService.registro(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Map<String, String>>builder()
                        .status(201)
                        .message("Usuario registrado")
                        .data(response)
                        .build());
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .status(200)
                        .message("Login exitoso")
                        .data(response)
                        .build());
    }

    // POST /api/auth/refresh
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {

        LoginResponse response = authService.renovarToken(request);

        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .status(200)
                        .message("Token renovado")
                        .data(response)
                        .build());
    }

    // POST /api/auth/logout
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody RefreshTokenRequest request) {

        authService.logout(request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(200)
                        .message("Sesión cerrada correctamente")
                        .build());
    }
    /*
     * // POST /api/auth/recuperar-password
     * 
     * @PostMapping("/recuperar-password")
     * public ResponseEntity<Map<String, String>> recuperarPassword(
     * 
     * @Valid @RequestBody RecuperacionRequest request) {
     * 
     * authService.solicitarRecuperacion(request);
     * 
     * // Siempre la misma respuesta — no revelar si el correo existe
     * return ResponseEntity.ok(Map.of(
     * "mensaje", "Si el correo está registrado recibirás un enlace"));
     * }
     * 
     * // POST /api/auth/nueva-password
     * 
     * @PostMapping("/nueva-password")
     * public ResponseEntity<Map<String, String>> nuevaPassword(
     * 
     * @Valid @RequestBody NuevaPasswordRequest request) {
     * 
     * authService.restablecerPassword(request);
     * return ResponseEntity.ok(Map.of(
     * "mensaje", "Contraseña actualizada correctamente"));
     * }
     * 
     * // POST /api/auth/verificar-correo
     * 
     * @PostMapping("/verificar-correo")
     * public ResponseEntity<Map<String, String>> verificarCorreo(
     * 
     * @RequestBody VerificarCodigoRequest request) {
     * 
     * authService.verificarCorreo(request.getCodigo(),
     * request.getNumeroDocumento());
     * return ResponseEntity.ok(Map.of(
     * "mensaje", "Correo verificado correctamente"));
     * }
     */
}