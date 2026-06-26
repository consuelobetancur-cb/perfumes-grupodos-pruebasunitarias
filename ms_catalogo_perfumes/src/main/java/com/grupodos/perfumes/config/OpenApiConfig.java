package com.grupodos.perfumes.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "codigoms-catalogo-perfumes",
        version = "1.0",
        description = "Proveedor central de perfumes y categorias. " +
                      "Los microservicios favoritos, carrito y pedidos consultan este servicio via HTTP " +
                      "cuando necesitan validar o enriquecer datos de un perfume. " +
                      "Puerto: 8082"
    )
)
@Configuration
public class OpenApiConfig {}

