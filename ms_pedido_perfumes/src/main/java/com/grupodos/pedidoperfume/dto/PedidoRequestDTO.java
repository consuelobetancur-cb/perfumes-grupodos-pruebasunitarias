package com.grupodos.pedidoperfume.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; // ¡Esto es clave!

@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class PedidoRequestDTO {
    @NotNull(message = "El perfumeId es obligatorio")
    private Long perfumeId;

    @NotBlank(message = "El cliente es obligatorio")
    private String cliente;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad minima es 1")
    private Integer cantidad;

    @NotBlank(message = "La opción de envío es obligatoria")
    private String opcionEnvio;

    private String cuponDescuento;
}