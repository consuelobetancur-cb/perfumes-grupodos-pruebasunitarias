package com.grupodos.resena.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResenaRequestDTO {

    @NotNull(message = "El ID del perfume es obligatorio")
    private Long perfumeId;

    @NotBlank(message = "El nombre del perfume es obligatorio")
    private String nombrePerfume;

    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer calificacion;

    @NotBlank(message = "El comentario no puede estar vacío")
    private String comentario;
}