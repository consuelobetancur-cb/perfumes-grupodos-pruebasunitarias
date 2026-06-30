package com.grupodos.inventario.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "API de Inventario - Perfulandia SPA",
                version = "1.0",
                description = "Microservicio encargado de gestionar la disponibilidad y stock físico de perfumes por sucursal. " +
                              "Endpoint clave para consultas: GET /api/inventario/perfume/{perfumeId}"
        )
)
@Configuration
public class OpenAPIConfig {} 