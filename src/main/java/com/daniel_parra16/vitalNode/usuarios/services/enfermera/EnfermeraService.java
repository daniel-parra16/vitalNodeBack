package com.daniel_parra16.vitalNode.usuarios.services.enfermera;

import java.util.List;

import com.daniel_parra16.vitalNode.usuarios.dtos.enfermera.CreateEnfermeraRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.enfermera.EnfermeraResponse;
import com.daniel_parra16.vitalNode.usuarios.dtos.enfermera.UpdateEnfermeraRequest;

public interface EnfermeraService {

    void create(String numeroDocumento, CreateEnfermeraRequest request);

    List<EnfermeraResponse> getAll();

    EnfermeraResponse getByDocumento(String numeroDocumento);

    void update(String numeroDocumento, UpdateEnfermeraRequest request);

    void delete(String numeroDocumento);
}