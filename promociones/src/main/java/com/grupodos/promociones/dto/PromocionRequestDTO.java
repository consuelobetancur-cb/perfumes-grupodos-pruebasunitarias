package com.grupodos.promociones.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PromocionRequestDTO {
    @NotBlank(message = "El código es obligatorio")
    private String codigo;

    @Min(value = 1, message = "El descuento mínimo es 1%")
    @Max(value = 100, message = "El descuento máximo es 100%")
    private Double porcentajeDescuento;

    @NotNull(message = "La fecha de expiración es obligatoria")
    private LocalDate fechaExpiracion;
}