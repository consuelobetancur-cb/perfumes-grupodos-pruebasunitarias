package com.grupodos.favorito.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PerfumeDTO {
    private Long id;
    private String nombre;
    private String marca;
    private BigDecimal precio;
    private Integer stock;
    private String categoriaNombre;
    
}
