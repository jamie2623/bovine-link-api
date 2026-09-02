package com.bovinelink.bovinelinkapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tabla: imagenes (ver diagrama E-R).
 * La clase conserva el nombre FotoPublicacion para no romper referencias;
 * renombrarla a Imagen es un refactor puro de Java que no toca la BD.
 */
@Entity
@Table(name = "imagenes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FotoPublicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(name = "foto_portada", nullable = false)
    private Boolean fotoPortada = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publicacion_id", nullable = false)
    private Publicacion publicacion;
}
