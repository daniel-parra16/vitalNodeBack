package com.daniel_parra16.vitalNode.auth.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.daniel_parra16.vitalNode.auth.models.Rol;
import com.daniel_parra16.vitalNode.auth.models.UsuarioAuth;

@Repository
public interface UsuarioAuthRepository extends MongoRepository<UsuarioAuth, String> {

    Optional<UsuarioAuth> findByNumeroDocumento(String numDoc);

    Boolean existsByNumeroDocumento(String numDoc);

    List<UsuarioAuth> findByRolesContaining(Rol rol);

    long countByRolesContaining(Rol rol);
}
