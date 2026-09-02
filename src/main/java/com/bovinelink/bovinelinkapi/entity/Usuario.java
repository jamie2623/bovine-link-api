package com.bovinelink.bovinelinkapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tabla: users (ver diagrama E-R).
 * Los nombres de campo Java se mantienen (nombre, correo, password...) para no
 * romper el resto del codigo; el mapeo a las columnas va en las anotaciones.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String nombre;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String correo;

    @Column(length = 20)
    private String telefono;

    @Column(nullable = false, length = 250)
    private String password;

    @Column(name = "remember_token", length = 100)
    private String rememberToken;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;
}
