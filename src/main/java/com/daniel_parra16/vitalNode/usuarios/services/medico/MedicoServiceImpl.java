package com.daniel_parra16.vitalNode.usuarios.services.medico;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daniel_parra16.vitalNode.auth.models.Rol;
import com.daniel_parra16.vitalNode.auth.models.UsuarioAuth;
import com.daniel_parra16.vitalNode.auth.repositories.UsuarioAuthRepository;
import com.daniel_parra16.vitalNode.exceptions.BadRequestException;
import com.daniel_parra16.vitalNode.exceptions.ConflictException;
import com.daniel_parra16.vitalNode.exceptions.NotFoundException;
import com.daniel_parra16.vitalNode.usuarios.dtos.medico.CreateMedicoRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.medico.MedicoResponse;
import com.daniel_parra16.vitalNode.usuarios.dtos.medico.UpdateMedicoRequest;
import com.daniel_parra16.vitalNode.usuarios.models.Medico;
import com.daniel_parra16.vitalNode.usuarios.repositories.MedicoRepository;
import com.daniel_parra16.vitalNode.usuarios.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicoServiceImpl implements MedicoService {

    private final MedicoRepository medicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioAuthRepository authRepository;

    @Override
    public void create(String numeroDocumento, CreateMedicoRequest request) {

        // 1. Validar usuario existe
        if (!usuarioRepository.existsByNumeroDocumento(numeroDocumento)) {
            throw new NotFoundException("Usuario no encontrado");
        }

        // 2. Validar auth existe
        UsuarioAuth auth = authRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new NotFoundException("Credenciales no encontradas"));

        // 3. Validar rol
        if (!auth.getRoles().contains(Rol.ROLE_DOCTOR)) {
            throw new BadRequestException("El usuario no tiene rol DOCTOR");
        }

        // 4. Validar duplicado
        if (medicoRepository.existsByNumeroDocumento(numeroDocumento)) {
            throw new ConflictException("El médico ya existe");
        }

        // 5. Crear
        Medico medico = Medico.builder()
                .numeroDocumento(numeroDocumento)
                .registroMedico(request.getRegistroMedico())
                .especialidad(request.getEspecialidad())
                .subEspecialidades(request.getSubEspecialidades())
                .aniosExperiencia(request.getAniosExperiencia())
                .activo(true)
                .build();

        medicoRepository.save(medico);
    }

    @Override
    public List<MedicoResponse> getAll() {
        return medicoRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public MedicoResponse getByDocumento(String numeroDocumento) {

        Medico medico = medicoRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new NotFoundException("Médico no encontrado"));

        return mapToResponse(medico);
    }

    @Override
    public void update(String numeroDocumento, UpdateMedicoRequest request) {

        Medico medico = medicoRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new NotFoundException("Médico no encontrado"));

        if (request.getRegistroMedico() != null) {
            medico.setRegistroMedico(request.getRegistroMedico());
        }

        if (request.getEspecialidad() != null) {
            medico.setEspecialidad(request.getEspecialidad());
        }

        if (request.getSubEspecialidades() != null) {
            medico.setSubEspecialidades(request.getSubEspecialidades());
        }

        if (request.getAniosExperiencia() != null) {
            medico.setAniosExperiencia(request.getAniosExperiencia());
        }

        if (request.getActivo() != null) {
            medico.setActivo(request.getActivo());
        }

        medicoRepository.save(medico);
    }

    @Override
    public void delete(String numeroDocumento) {

        Medico medico = medicoRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new NotFoundException("Médico no encontrado"));

        medico.setActivo(false);
        medicoRepository.save(medico);
    }

    private MedicoResponse mapToResponse(Medico m) {
        return MedicoResponse.builder()
                .numeroDocumento(m.getNumeroDocumento())
                .registroMedico(m.getRegistroMedico())
                .especialidad(m.getEspecialidad())
                .subEspecialidades(m.getSubEspecialidades())
                .aniosExperiencia(m.getAniosExperiencia())
                .activo(m.isActivo())
                .build();
    }
}