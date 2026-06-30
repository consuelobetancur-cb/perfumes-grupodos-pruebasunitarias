package com.grupodos.usuarios.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "API de Usuarios - Perfulandia SPA",
                version = "1.0",
                description = "Microservicio encargado de la gestión de clientes y administradores de la tienda Perfulandia. " +
                              "Provee los datos necesarios para logística (direcciones de envío) y validación de identidad. " +
                              "Endpoint clave para integración con otros microservicios: GET /api/usuarios/email/{email}"
        )
)
@Configuration
public class OpenAPIConfig {}