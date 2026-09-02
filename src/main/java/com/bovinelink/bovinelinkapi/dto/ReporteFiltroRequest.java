package com.bovinelink.bovinelinkapi.dto;

import com.bovinelink.bovinelinkapi.entity.Publicacion;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Filtros de GET /api/reportes y GET /api/reportes/exportar-pdf.
 * Se enlaza desde los query params (?desde=&hasta=&estado=), todos opcionales.
 *   desde/hasta -> formato ISO yyyy-MM-dd
 *   estado      -> "en_venta" | "vendido" (acepta mayus/minus y guiones)
 */
@Data
public class ReporteFiltroRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate desde;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate hasta;

    private String estado;

    public LocalDateTime desdeComoInicioDelDia() {
        return desde == null ? null : desde.atStartOfDay();
    }

    public LocalDateTime hastaComoFinDelDia() {
        return hasta == null ? null : hasta.atTime(LocalTime.MAX);
    }

    public Publicacion.Estado estadoComoEnum() {
        if (estado == null || estado.isBlank()) {
            return null;
        }
        // Publicacion.Estado.fromString lanza IllegalArgumentException si el valor
        // no es valido -> GlobalExceptionHandler responde 400.
        return Publicacion.Estado.fromString(estado);
    }
}
