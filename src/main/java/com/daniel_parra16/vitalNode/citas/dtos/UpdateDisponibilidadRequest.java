package com.daniel_parra16.vitalNode.citas.dtos;

import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.Data;

@Data
public class UpdateDisponibilidadRequest {

    private DayOfWeek dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}