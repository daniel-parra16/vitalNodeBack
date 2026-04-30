package com.daniel_parra16.vitalNode.usuarios.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daniel_parra16.vitalNode.exceptions.ApiResponse;
import com.daniel_parra16.vitalNode.usuarios.dtos.enfermera.CreateEnfermeraRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.enfermera.EnfermeraResponse;
import com.daniel_parra16.vitalNode.usuarios.dtos.enfermera.UpdateEnfermeraRequest;
import com.daniel_parra16.vitalNode.usuarios.services.enfermera.EnfermeraService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/enfermeras")
@RequiredArgsConstructor
public class EnfermeraController {

        private final EnfermeraService enfermeraService;

        @PostMapping("/{numeroDocumento}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<Void>> create(
                        @PathVariable String numeroDocumento,
                        @Valid @RequestBody CreateEnfermeraRequest request) {

                enfermeraService.create(numeroDocumento, request);

                return ResponseEntity.status(201)
                                .body(ApiResponse.<Void>builder()
                                                .status(201)
                                                .message("Enfermera creada correctamente")
                                                .build());
        }

        @GetMapping("/{numeroDocumento}")
        @PreAuthorize("hasAnyRole('ADMIN','NURSE')")
        public ResponseEntity<ApiResponse<EnfermeraResponse>> get(
                        @PathVariable String numeroDocumento) {

                return ResponseEntity.ok(
                                ApiResponse.<EnfermeraResponse>builder()
                                                .status(200)
                                                .message("Enfermera encontrada")
                                                .data(enfermeraService.getByDocumento(numeroDocumento))
                                                .build());
        }

        @PutMapping("/{numeroDocumento}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<Void>> update(
                        @PathVariable String numeroDocumento,
                        @RequestBody UpdateEnfermeraRequest request) {

                enfermeraService.update(numeroDocumento, request);

                return ResponseEntity.ok(
                                ApiResponse.<Void>builder()
                                                .status(200)
                                                .message("Enfermera actualizada")
                                                .build());
        }

        @DeleteMapping("/{numeroDocumento}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<Void>> delete(
                        @PathVariable String numeroDocumento) {

                enfermeraService.delete(numeroDocumento);

                return ResponseEntity.ok(
                                ApiResponse.<Void>builder()
                                                .status(200)
                                                .message("Enfermera eliminada")
                                                .build());
        }
}