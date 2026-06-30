package com.grupodos.promociones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AplicarDescuentoDTO {
    private Double montoDescuento;
    private Double totalConDescuento;
    private String mensaje;
}