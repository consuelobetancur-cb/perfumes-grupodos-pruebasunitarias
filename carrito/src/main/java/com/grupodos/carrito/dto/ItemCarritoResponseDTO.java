package com.grupodos.carrito.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ItemCarritoResponseDTO {
    private Long id;
    private Long perfumeId;
    private String nombrePerfume;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
