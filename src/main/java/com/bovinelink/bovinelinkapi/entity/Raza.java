package com.bovinelink.bovinelinkapi.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "razas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Raza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 255)
    private String nombre;

    @NotNull(message = "La categoria es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Categoria categoria;

    public enum Categoria {
        LECHE("leche"),
        CARNE("carne"),
        DOBLE_PROPOSITO("doble_proposito");

        private final String valor;

        Categoria(String valor) {
            this.valor = valor;
        }

        @JsonValue
        public String getValor() {
            return valor;
        }

        @JsonCreator
        public static Categoria fromString(String valor) {
            if (valor == null) {
                return null;
            }

            String normalizado = valor.trim()
                    .toUpperCase()
                    .replace(" ", "_")
                    .replace("-", "_");

            return Categoria.valueOf(normalizado);
        }
    }
}
