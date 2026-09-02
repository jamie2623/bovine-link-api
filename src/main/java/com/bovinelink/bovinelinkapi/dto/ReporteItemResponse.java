package com.bovinelink.bovinelinkapi.dto;

import com.bovinelink.bovinelinkapi.entity.Publicacion;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Fila de la tabla de GET /api/reportes.
 * Se construye directamente en la consulta JPQL (proyeccion), por eso el
 * orden de los campos debe coincidir con el constructor usado en
 * {@code EstadisticaRepository#buscarReporte}.
 */
@Data
@AllArgsConstructor
public class ReporteItemResponse {

    private Long id;
    private String titulo;
    private String raza;
    private BigDecimal precio;
    private String ubicacion;
    private Publicacion.Estado estado;
    private String vendedor;
    private LocalDateTime fechaPublicacion;
}
