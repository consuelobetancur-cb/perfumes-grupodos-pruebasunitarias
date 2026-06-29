package com.grupodos.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventarioRequestDTO {
    @NotNull(message = "El ID del perfume es obligatorio")
    private Long perfumeId;
    @NotBlank(message = "La sucursal es obligatoria")
    private String sucursal;
    @NotNull(message = "La cantidad en stock es obligatoria")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer cantidad;
}