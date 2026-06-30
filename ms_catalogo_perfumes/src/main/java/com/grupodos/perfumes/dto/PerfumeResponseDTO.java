package com.grupodos.perfumes.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfumeResponseDTO {
    private Long id;
    private String nombre;
    private String marca;
    private BigDecimal precio;
    private Integer stock;
    private String categoriaNombre;
}
