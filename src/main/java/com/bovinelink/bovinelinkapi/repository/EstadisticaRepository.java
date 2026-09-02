package com.bovinelink.bovinelinkapi.repository;

import com.bovinelink.bovinelinkapi.dto.ConteoResponse;
import com.bovinelink.bovinelinkapi.dto.ReporteItemResponse;
import com.bovinelink.bovinelinkapi.entity.Publicacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio de SOLO LECTURA para Dashboard y Reportes (Persona 3).
 * Es una interfaz aparte de PublicacionRepository para no chocar con Persona 2;
 * apunta a la misma entidad Publicacion.
 */
public interface EstadisticaRepository extends Repository<Publicacion, Long> {

    /** Total de publicaciones (cualquier estado). */
    long count();

    /** Cuenta publicaciones por estado (EN_VENTA / VENDIDO). */
    long countByEstado(Publicacion.Estado estado);

    /** Suma de precios de las publicaciones en el estado dado; 0 si no hay. */
    @Query("SELECT COALESCE(SUM(p.precio), 0) FROM Publicacion p WHERE p.estado = :estado")
    BigDecimal sumarPrecioPorEstado(@Param("estado") Publicacion.Estado estado);

    /** Conteo de publicaciones agrupado por ubicacion (grafico de barras). */
    @Query("""
            SELECT new com.bovinelink.bovinelinkapi.dto.ConteoResponse(p.ubicacion, COUNT(p))
            FROM Publicacion p
            GROUP BY p.ubicacion
            ORDER BY COUNT(p) DESC, p.ubicacion ASC
            """)
    List<ConteoResponse> contarPorUbicacion();

    /** Conteo de publicaciones agrupado por raza (grafico de pie). */
    @Query("""
            SELECT new com.bovinelink.bovinelinkapi.dto.ConteoResponse(r.nombre, COUNT(p))
            FROM Publicacion p
            JOIN p.raza r
            GROUP BY r.nombre
            ORDER BY COUNT(p) DESC, r.nombre ASC
            """)
    List<ConteoResponse> contarPorRaza();

    /**
     * Tabla de reportes con filtros opcionales (todos pueden ir null).
     * Proyecta directamente a ReporteItemResponse evitando lazy loading.
     */
    @Query("""
            SELECT new com.bovinelink.bovinelinkapi.dto.ReporteItemResponse(
                p.id, p.titulo, r.nombre, p.precio, p.ubicacion, p.estado, u.nombre, p.fechaPublicacion)
            FROM Publicacion p
            JOIN p.raza r
            JOIN p.usuario u
            WHERE (CAST(:desde AS timestamp) IS NULL OR p.fechaPublicacion >= :desde)
              AND (CAST(:hasta AS timestamp) IS NULL OR p.fechaPublicacion <= :hasta)
              AND (CAST(:estado AS string) IS NULL OR p.estado = :estado)
            ORDER BY p.fechaPublicacion DESC
            """)
    List<ReporteItemResponse> buscarReporte(@Param("desde") LocalDateTime desde,
                                            @Param("hasta") LocalDateTime hasta,
                                            @Param("estado") Publicacion.Estado estado);
}
