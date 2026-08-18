package com.bovinelink.bovinelinkapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long id;
    private String nombre;
    private String correo;
    private String rol;
}