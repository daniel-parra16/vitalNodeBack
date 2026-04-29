package com.daniel_parra16.vitalNode.auth.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.daniel_parra16.vitalNode.auth.models.Sesion;

@Repository
public interface SesionRepository extends MongoRepository<Sesion, String> {

    Optional<Sesion> findByRefreshToken(String refreshToken);

    List<Sesion> findByUsuarioIdAndActivaTrue(String usuarioId);

    Optional<Sesion> findTopByUsuarioIdOrderByCreatedAtDesc(String usuarioId);
}
