package com.grupodos.favorito.dto;


import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FavoritoResponseDTO {
    private Long id;
    private Long perfumeId;
    private String nombre;
    private String usuario; 
    private LocalDateTime fechaAgregado;
}
