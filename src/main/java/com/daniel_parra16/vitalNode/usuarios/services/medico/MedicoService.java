package com.daniel_parra16.vitalNode.usuarios.services.medico;

import java.util.List;

import com.daniel_parra16.vitalNode.usuarios.dtos.medico.CreateMedicoRequest;
import com.daniel_parra16.vitalNode.usuarios.dtos.medico.MedicoResponse;
import com.daniel_parra16.vitalNode.usuarios.dtos.medico.UpdateMedicoRequest;

public interface MedicoService {

    void create(String numeroDocumento, CreateMedicoRequest request);

    List<MedicoResponse> getAll();

    MedicoResponse getByDocumento(String numeroDocumento);

    void update(String numeroDocumento, UpdateMedicoRequest request);

    void delete(String numeroDocumento);
}