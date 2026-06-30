package com.grupodos.pagos.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PagoResponseDTO {
    
    private Long id;
    private Long pedidoId;
    private Double monto;
    private String metodoPago;
    private String estado;
    private LocalDateTime fechaPago;
}