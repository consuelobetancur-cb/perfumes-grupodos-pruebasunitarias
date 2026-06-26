package com.grupodos.favorito.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FavoritoRequestDTO {
    
    @NotNull(message = "El perfumeId es obligatorio")
    private Long perfumeId;

    @NotBlank(message = "El usuario no puede estar vacío")
    private String usuario;
}
