package com.grupodos.inventario.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InventarioResponseDTO {
    private Long id;
    private Long perfumeId;
    private String sucursal;
    private Integer cantidad;
    private LocalDateTime fechaActualizacion;
}