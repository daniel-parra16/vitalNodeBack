package com.daniel_parra16.vitalNode.citas.services;

import java.util.List;

import com.daniel_parra16.vitalNode.citas.dtos.CreateDisponibilidadRequest;
import com.daniel_parra16.vitalNode.citas.dtos.DisponibilidadResponse;
import com.daniel_parra16.vitalNode.citas.dtos.UpdateDisponibilidadRequest;

public interface DisponibilidadService {

    void create(String medicoDocumento, CreateDisponibilidadRequest request);

    List<DisponibilidadResponse> getByMedico(String medicoDocumento);

    void update(String id, UpdateDisponibilidadRequest request);

    void delete(String id);
}