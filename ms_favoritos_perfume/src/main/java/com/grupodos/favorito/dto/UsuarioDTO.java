package com.grupodos.favorito.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private Boolean activo;
}


