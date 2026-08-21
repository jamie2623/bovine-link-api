package com.bovinelink.bovinelinkapi.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "publicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false, length = 150)
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado = Estado.EN_VENTA;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "raza_id", nullable = false)
    private Raza raza;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion = LocalDateTime.now();

    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotoPublicacion> fotos = new ArrayList<>();

    public enum Estado {
        EN_VENTA("en_venta"),
        VENDIDO("vendido");

        private final String valor;

        Estado(String valor) {
            this.valor = valor;
        }

        @JsonValue
        public String getValor() {
            return valor;
        }

        @JsonCreator
        public static Estado fromString(String valor) {
            if (valor == null) {
                return null;
            }

            String normalizado = valor.trim()
                    .toUpperCase()
                    .replace(" ", "_")
                    .replace("-", "_");

            return Estado.valueOf(normalizado);
        }
    }
}
