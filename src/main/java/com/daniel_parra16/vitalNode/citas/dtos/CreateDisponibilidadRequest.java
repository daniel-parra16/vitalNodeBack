package com.daniel_parra16.vitalNode.citas.dtos;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDisponibilidadRequest {

    @NotNull(message = "El día es obligatorio")
    private DayOfWeek dia;

    @NotNull(message = "Hora inicio obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "Hora fin obligatoria")
    private LocalTime horaFin;
}