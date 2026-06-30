package com.grupodos.resena.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResenaPromedioDTO {
    private Long perfumeId;
    private Double promedioCalificacion;
    private Long totalResenas; 
}