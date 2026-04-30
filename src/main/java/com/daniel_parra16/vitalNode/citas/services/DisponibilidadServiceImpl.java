package com.daniel_parra16.vitalNode.citas.services;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.daniel_parra16.vitalNode.citas.dtos.CreateDisponibilidadRequest;
import com.daniel_parra16.vitalNode.citas.dtos.DisponibilidadResponse;
import com.daniel_parra16.vitalNode.citas.dtos.UpdateDisponibilidadRequest;
import com.daniel_parra16.vitalNode.citas.models.DisponibilidadMedica;
import com.daniel_parra16.vitalNode.citas.repositories.DisponibilidadRepository;
import com.daniel_parra16.vitalNode.exceptions.BadRequestException;
import com.daniel_parra16.vitalNode.exceptions.NotFoundException;
import com.daniel_parra16.vitalNode.usuarios.models.Medico;
import com.daniel_parra16.vitalNode.usuarios.repositories.MedicoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DisponibilidadServiceImpl implements DisponibilidadService {

    private final DisponibilidadRepository disponibilidadRepository;
    private final MedicoRepository medicoRepository;

    @Override
    public void create(String medicoDocumento, CreateDisponibilidadRequest request) {

        // 1. Validar médico
        Medico medico = medicoRepository.findByNumeroDocumento(medicoDocumento)
                .orElseThrow(() -> new NotFoundException("Médico no encontrado"));

        if (!medico.isActivo()) {
            throw new BadRequestException("El médico está inactivo");
        }

        // 2. Validar horas
        if (request.getHoraFin().isBefore(request.getHoraInicio())) {
            throw new BadRequestException("La hora fin debe ser mayor a la hora inicio");
        }

        // 3. Validar solapamiento
        List<DisponibilidadMedica> existentes = disponibilidadRepository.findByMedicoDocumentoAndDiaAndActivoTrue(
                medicoDocumento,
                request.getDia());

        for (DisponibilidadMedica d : existentes) {

            boolean solapa = request.getHoraInicio().isBefore(d.getHoraFin()) &&
                    request.getHoraFin().isAfter(d.getHoraInicio());

            if (solapa) {
                throw new BadRequestException("El horario se solapa con otro existente");
            }
        }

        // 4. Guardar
        DisponibilidadMedica disponibilidad = DisponibilidadMedica.builder()
                .medicoDocumento(medicoDocumento)
                .dia(request.getDia())
                .horaInicio(request.getHoraInicio())
                .horaFin(request.getHoraFin())
                .activo(true)
                .build();

        disponibilidadRepository.save(disponibilidad);
    }

    @Override
    public List<DisponibilidadResponse> getByMedico(String medicoDocumento) {

        return disponibilidadRepository
                .findByMedicoDocumentoAndActivoTrue(medicoDocumento)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void update(String id, UpdateDisponibilidadRequest request) {

        DisponibilidadMedica disponibilidad = disponibilidadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Disponibilidad no encontrada"));

        if (!disponibilidad.isActivo()) {
            throw new BadRequestException("La disponibilidad está inactiva");
        }

        DayOfWeek dia = request.getDia() != null ? request.getDia() : disponibilidad.getDia();
        LocalTime inicio = request.getHoraInicio() != null ? request.getHoraInicio() : disponibilidad.getHoraInicio();
        LocalTime fin = request.getHoraFin() != null ? request.getHoraFin() : disponibilidad.getHoraFin();

        // validar horas
        if (fin.isBefore(inicio)) {
            throw new BadRequestException("Hora fin inválida");
        }

        // validar solapamiento (excluyendo el mismo id)
        List<DisponibilidadMedica> existentes = disponibilidadRepository.findByMedicoDocumentoAndDiaAndActivoTrue(
                disponibilidad.getMedicoDocumento(),
                dia);

        for (DisponibilidadMedica d : existentes) {

            if (d.getId().equals(id))
                continue;

            boolean solapa = inicio.isBefore(d.getHoraFin()) &&
                    fin.isAfter(d.getHoraInicio());

            if (solapa) {
                throw new BadRequestException("El horario se solapa con otro");
            }
        }

        disponibilidad.setDia(dia);
        disponibilidad.setHoraInicio(inicio);
        disponibilidad.setHoraFin(fin);

        disponibilidadRepository.save(disponibilidad);
    }

    @Override
    public void delete(String id) {

        DisponibilidadMedica disponibilidad = disponibilidadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Disponibilidad no encontrada"));

        disponibilidad.setActivo(false);
        disponibilidadRepository.save(disponibilidad);
    }

    private DisponibilidadResponse mapToResponse(DisponibilidadMedica d) {
        return DisponibilidadResponse.builder()
                .id(d.getId())
                .medicoDocumento(d.getMedicoDocumento())
                .dia(d.getDia())
                .horaInicio(d.getHoraInicio())
                .horaFin(d.getHoraFin())
                .activo(d.isActivo())
                .build();
    }
}