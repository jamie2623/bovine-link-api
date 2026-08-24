package com.bovinelink.bovinelinkapi.dto;

import com.bovinelink.bovinelinkapi.entity.FotoPublicacion;
import com.bovinelink.bovinelinkapi.entity.Publicacion;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PublicacionResponse {

    private Long id;
    private String titulo;
    private String descripcion;
    private BigDecimal precio;
    private String ubicacion;
    private Publicacion.Estado estado;
    private Long usuarioId;
    private String usuarioNombre;
    private Long razaId;
    private String razaNombre;
    private LocalDateTime fechaPublicacion;
    private List<String> fotos;

    public static PublicacionResponse fromEntity(Publicacion publicacion) {
        return new PublicacionResponse(
                publicacion.getId(),
                publicacion.getTitulo(),
                publicacion.getDescripcion(),
                publicacion.getPrecio(),
                publicacion.getUbicacion(),
                publicacion.getEstado(),
                publicacion.getUsuario().getId(),
                publicacion.getUsuario().getNombre(),
                publicacion.getRaza().getId(),
                publicacion.getRaza().getNombre(),
                publicacion.getFechaPublicacion(),
                publicacion.getFotos().stream()
                        .map(FotoPublicacion::getUrl)
                        .toList()
        );
    }
}
