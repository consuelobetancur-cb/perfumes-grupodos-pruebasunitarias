package com.grupodos.resena.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor 
@NoArgsConstructor
public class PerfumeDTO {
    private Long id;
    private String nombre;
    private String marca; 
    private BigDecimal precio;
    private String categoriaNombre;
}