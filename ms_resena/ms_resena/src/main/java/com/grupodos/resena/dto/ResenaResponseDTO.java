package com.grupodos.resena.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResenaResponseDTO {
    private Long id;
    private Long perfumeId;
    private String nombrePerfume;
    private String usuario;
    private Integer calificacion;
    private String comentario;
    private LocalDateTime fechaResena;
}