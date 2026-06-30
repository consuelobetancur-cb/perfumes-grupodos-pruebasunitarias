package com.grupodos.pedidoperfume.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PedidoResponseDTO {

    private Long id;
    private String cliente;
    private Long perfumeId;
    private String nombrePerfume;
    private LocalDateTime fechaPedido;
    private String estadoPedido; 
    private BigDecimal total;
    private Integer cantidad;
    private String opcionEnvio;  
    private String cuponDescuento; 
}
