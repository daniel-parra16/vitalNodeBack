package com.daniel_parra16.vitalNode.auth.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "sesiones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sesion {

    @Id
    private String id;

    @Indexed
    private String usuarioId;

    private String refreshToken;

    private LocalDateTime ultimaActividad; // para el interruptor de inactividad

    private LocalDateTime expiraEn;

    private boolean activa;

    @CreatedDate
    private LocalDateTime createdAt;
}