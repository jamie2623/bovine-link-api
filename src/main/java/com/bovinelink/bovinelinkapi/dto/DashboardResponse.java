package com.bovinelink.bovinelinkapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Respuesta de GET /api/dashboard/resumen.
 */
@Data
@AllArgsConstructor
public class DashboardResponse {

    /** Total de publicaciones registradas (cualquier estado). */
    private long totalPublicaciones;

    /** Usuarios registrados en la plataforma. */
    private long usuariosRegistrados;

    /** Ganado vendido = publicaciones en estado VENDIDO. */
    private long ganadoVendido;

    /** Publicaciones que siguen a la venta. */
    private long publicacionesEnVenta;

    /** Suma de precios de las publicaciones vendidas. */
    private BigDecimal montoTotalVendido;
}
