package com.daniel_parra16.vitalNode.citas.controller;

import java.util.List;
import java.util.Map;

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

import com.daniel_parra16.vitalNode.citas.dtos.CreateDisponibilidadRequest;
import com.daniel_parra16.vitalNode.citas.dtos.DisponibilidadResponse;
import com.daniel_parra16.vitalNode.citas.dtos.UpdateDisponibilidadRequest;
import com.daniel_parra16.vitalNode.citas.services.DisponibilidadService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/disponibilidad")
@RequiredArgsConstructor
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    // CREAR
    @PostMapping("/{medicoDocumento}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> create(
            @PathVariable String medicoDocumento,
            @RequestBody @Valid CreateDisponibilidadRequest request) {

        disponibilidadService.create(medicoDocumento, request);

        return ResponseEntity.ok(Map.of("message", "Disponibilidad creada"));
    }

    // LISTAR POR MÉDICO
    @GetMapping("/{medicoDocumento}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<DisponibilidadResponse>> getByMedico(
            @PathVariable String medicoDocumento) {

        return ResponseEntity.ok(
                disponibilidadService.getByMedico(medicoDocumento));
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> update(
            @PathVariable String id,
            @RequestBody UpdateDisponibilidadRequest request) {

        disponibilidadService.update(id, request);

        return ResponseEntity.ok(Map.of("message", "Disponibilidad actualizada"));
    }

    // ELIMINAR (soft)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String id) {

        disponibilidadService.delete(id);

        return ResponseEntity.ok(Map.of("message", "Disponibilidad eliminada"));
    }
}