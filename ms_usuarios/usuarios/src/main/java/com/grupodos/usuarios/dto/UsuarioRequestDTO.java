package com.grupodos.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene formato válido")
    @Size(max = 150, message = "El email no debe superar los 150 caracteres")
    private String email;

    @Size(max = 20, message = "El teléfono no debe superar los 20 caracteres")
    private String telefono;

    @NotBlank(message = "La dirección de envío es obligatoria")
    @Size(max = 255, message = "La dirección de envío no debe superar los 255 caracteres")
    private String direccionEnvio;
}