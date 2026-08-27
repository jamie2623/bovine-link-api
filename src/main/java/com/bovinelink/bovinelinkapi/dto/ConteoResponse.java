package com.bovinelink.bovinelinkapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Item generico para los graficos del dashboard.
 * - por-ubicacion: etiqueta = ubicacion
 * - por-raza:      etiqueta = nombre de la raza
 */
@Data
@AllArgsConstructor
public class ConteoResponse {

    private String etiqueta;
    private Long total;
}
