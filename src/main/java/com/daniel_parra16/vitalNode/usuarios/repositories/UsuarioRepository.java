package com.daniel_parra16.vitalNode.usuarios.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.daniel_parra16.vitalNode.usuarios.models.Usuario;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByNumeroDocumento(String numeroDocumento);

    boolean existsByNumeroDocumento(String numeroDocumento);
}