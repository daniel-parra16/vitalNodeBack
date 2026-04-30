package com.daniel_parra16.vitalNode.usuarios.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.daniel_parra16.vitalNode.usuarios.models.Enfermera;

@Repository
public interface EnfermeraRepository extends MongoRepository<Enfermera, String> {

    Optional<Enfermera> findByNumeroDocumento(String numeroDocumento);

    boolean existsByNumeroDocumento(String numeroDocumento);

    List<Enfermera> findByActivoTrue();
}