package com.daniel_parra16.vitalNode.citas.models;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "DisponibilidadMedica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponibilidadMedica {

    @Id
    private String id;

    private String medicoDocumento;

    private DayOfWeek dia;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private boolean activo;
}