package com.daniel_parra16.vitalNode.usuarios.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Documento {

    public TipoDoc tipo;

    public String numero;

}