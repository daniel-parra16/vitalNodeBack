package com.daniel_parra16.vitalNode.citas.repositories;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.daniel_parra16.vitalNode.citas.models.DisponibilidadMedica;

@Repository
public interface DisponibilidadRepository extends MongoRepository<DisponibilidadMedica, String> {

    List<DisponibilidadMedica> findByMedicoDocumentoAndActivoTrue(String medicoDocumento);

    List<DisponibilidadMedica> findByDiaAndActivoTrue(DayOfWeek dia);

    List<DisponibilidadMedica> findByMedicoDocumentoAndDiaAndActivoTrue(
            String medicoDocumento,
            DayOfWeek dia);

    @Query("""
            {
                'medicoDocumento': ?0,
                'dia': ?1,
                'activo': true,
                'horaInicio': { $lte: ?2 },
                'horaFin': { $gte: ?3 }
            }
            """)
    List<DisponibilidadMedica> findDisponibilidadEnRango(
            String medicoDocumento,
            DayOfWeek dia,
            LocalTime inicio,
            LocalTime fin);
}