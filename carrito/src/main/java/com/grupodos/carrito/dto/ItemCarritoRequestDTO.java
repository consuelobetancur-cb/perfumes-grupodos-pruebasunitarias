package com.grupodos.carrito.dto;

import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
public class ItemCarritoRequestDTO {

    @NotNull(message = "El perfumeId es obligatorio")
    private Long perfumeId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer cantidad;
}
