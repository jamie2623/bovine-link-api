package com.bovinelink.bovinelinkapi.dto;

import com.bovinelink.bovinelinkapi.entity.Publicacion;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PublicacionRequest {

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 255)
    private String titulo;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 5000)
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal precio;

    @NotBlank(message = "La ubicacion es obligatoria")
    @Size(max = 255)
    private String ubicacion;

    private Publicacion.Estado estado = Publicacion.Estado.EN_VENTA;

    @NotNull(message = "La raza es obligatoria")
    private Long razaId;

    @Size(max = 5)
    private List<String> fotos;
}
