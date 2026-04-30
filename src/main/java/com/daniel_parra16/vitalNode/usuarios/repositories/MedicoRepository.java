package com.daniel_parra16.vitalNode.usuarios.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.daniel_parra16.vitalNode.usuarios.models.Medico;

public interface MedicoRepository extends MongoRepository<Medico, String> {

    Optional<Medico> findByNumeroDocumento(String numeroDocumento);

    boolean existsByNumeroDocumento(String numeroDocumento);
}