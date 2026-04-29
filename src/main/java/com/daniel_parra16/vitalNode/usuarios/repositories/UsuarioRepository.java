package com.daniel_parra16.vitalNode.usuarios.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.daniel_parra16.vitalNode.usuarios.models.Usuario;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);

    // Busca dentro del objeto embebido doc por el campo numero
    @Query("{ 'doc.numero': ?0 }")
    Optional<Usuario> findByNumeroDocumento(String numeroDocumento);

    @Query(value = "{ 'doc.numero': ?0 }", exists = true)
    boolean existsByNumeroDocumento(String numeroDocumento);

}