package com.grupodos.pagos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PagoRequestDTO {

    @NotNull(message = "El ID del pedido es obligatorio")
    private Long pedidoId;
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")

    private Double monto;
    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;
}