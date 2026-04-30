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
import com.daniel_parra16.vitalNode.usuarios.dtos.medico.CreateMedicoRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.medico.MedicoResponse;
import com.daniel_parra16.vitalNode.usuarios.dtos.medico.UpdateMedicoRequest;
import com.daniel_parra16.vitalNode.usuarios.services.medico.MedicoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

        private final MedicoService medicoService;

        // 🔥 CREAR
        @PostMapping("/{numeroDocumento}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<Void>> create(
                        @PathVariable String numeroDocumento,
                        @Valid @RequestBody CreateMedicoRequest request) {

                medicoService.create(numeroDocumento, request);

                return ResponseEntity.status(201)
                                .body(ApiResponse.<Void>builder()
                                                .status(201)
                                                .message("Médico creado correctamente")
                                                .build());
        }

        // 🔥 GET
        @GetMapping("/{numeroDocumento}")
        @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
        public ResponseEntity<ApiResponse<MedicoResponse>> get(
                        @PathVariable String numeroDocumento) {

                return ResponseEntity.ok(
                                ApiResponse.<MedicoResponse>builder()
                                                .status(200)
                                                .message("Médico encontrado")
                                                .data(medicoService.getByDocumento(numeroDocumento))
                                                .build());
        }

        // 🔥 UPDATE
        @PutMapping("/{numeroDocumento}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<Void>> update(
                        @PathVariable String numeroDocumento,
                        @Valid @RequestBody UpdateMedicoRequest request) {

                medicoService.update(numeroDocumento, request);

                return ResponseEntity.ok(
                                ApiResponse.<Void>builder()
                                                .status(200)
                                                .message("Médico actualizado")
                                                .build());
        }

        // 🔥 DELETE (soft)
        @DeleteMapping("/{numeroDocumento}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<Void>> delete(
                        @PathVariable String numeroDocumento) {

                medicoService.delete(numeroDocumento);

                return ResponseEntity.ok(
                                ApiResponse.<Void>builder()
                                                .status(200)
                                                .message("Médico eliminado")
                                                .build());
        }
}