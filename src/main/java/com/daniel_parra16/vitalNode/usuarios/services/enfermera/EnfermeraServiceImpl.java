package com.daniel_parra16.vitalNode.usuarios.services.enfermera;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daniel_parra16.vitalNode.auth.models.Rol;
import com.daniel_parra16.vitalNode.auth.models.UsuarioAuth;
import com.daniel_parra16.vitalNode.auth.repositories.UsuarioAuthRepository;
import com.daniel_parra16.vitalNode.exceptions.BadRequestException;
import com.daniel_parra16.vitalNode.exceptions.ConflictException;
import com.daniel_parra16.vitalNode.exceptions.NotFoundException;
import com.daniel_parra16.vitalNode.usuarios.dtos.enfermera.CreateEnfermeraRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.enfermera.EnfermeraResponse;
import com.daniel_parra16.vitalNode.usuarios.dtos.enfermera.UpdateEnfermeraRequest;
import com.daniel_parra16.vitalNode.usuarios.models.Enfermera;
import com.daniel_parra16.vitalNode.usuarios.repositories.EnfermeraRepository;
import com.daniel_parra16.vitalNode.usuarios.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnfermeraServiceImpl implements EnfermeraService {

    private final EnfermeraRepository enfermeraRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioAuthRepository authRepository;

    @Override
    public void create(String numeroDocumento, CreateEnfermeraRequest request) {

        if (!usuarioRepository.existsByNumeroDocumento(numeroDocumento)) {
            throw new NotFoundException("Usuario no encontrado");
        }

        UsuarioAuth auth = authRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new NotFoundException("Credenciales no encontradas"));

        if (!auth.getRoles().contains(Rol.ROLE_NURSE)) {
            throw new BadRequestException("El usuario no tiene rol NURSE");
        }

        if (enfermeraRepository.existsByNumeroDocumento(numeroDocumento)) {
            throw new ConflictException("La enfermera ya existe");
        }

        Enfermera enfermera = Enfermera.builder()
                .numeroDocumento(numeroDocumento)
                .registroProfesional(request.getRegistroProfesional())
                .area(request.getArea())
                .habilidades(request.getHabilidades())
                .activo(true)
                .build();

        enfermeraRepository.save(enfermera);
    }

    @Override
    public List<EnfermeraResponse> getAll() {
        return enfermeraRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public EnfermeraResponse getByDocumento(String numeroDocumento) {

        Enfermera enfermera = enfermeraRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new NotFoundException("Enfermera no encontrada"));

        return mapToResponse(enfermera);
    }

    @Override
    public void update(String numeroDocumento, UpdateEnfermeraRequest request) {

        Enfermera enfermera = enfermeraRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new NotFoundException("Enfermera no encontrada"));

        if (request.getRegistroProfesional() != null) {
            enfermera.setRegistroProfesional(request.getRegistroProfesional());
        }

        if (request.getArea() != null) {
            enfermera.setArea(request.getArea());
        }

        if (request.getHabilidades() != null) {
            enfermera.setHabilidades(request.getHabilidades());
        }

        if (request.getActivo() != null) {
            enfermera.setActivo(request.getActivo());
        }

        enfermeraRepository.save(enfermera);
    }

    @Override
    public void delete(String numeroDocumento) {

        Enfermera enfermera = enfermeraRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new NotFoundException("Enfermera no encontrada"));

        enfermera.setActivo(false);
        enfermeraRepository.save(enfermera);
    }

    private EnfermeraResponse mapToResponse(Enfermera e) {
        return EnfermeraResponse.builder()
                .numeroDocumento(e.getNumeroDocumento())
                .registroProfesional(e.getRegistroProfesional())
                .area(e.getArea())
                .habilidades(e.getHabilidades())
                .activo(e.isActivo())
                .build();
    }
}