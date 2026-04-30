package com.daniel_parra16.vitalNode.citas.dtos;

import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisponibilidadResponse {

    private String id;
    private String medicoDocumento;
    private DayOfWeek dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private boolean activo;
}