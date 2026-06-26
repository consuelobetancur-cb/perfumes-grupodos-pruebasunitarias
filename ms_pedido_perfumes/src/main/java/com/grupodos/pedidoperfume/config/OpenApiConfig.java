package com.grupodos.pedidoperfume.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "codigoms-pedidos",
        version = "1.0",
        description = "Gestiona ordenes de compra de perfumes. " +
                      "Depende de ms-usuarios (valida cliente via FeignClient) " +
                      "y de ms-catalogo-perfume (valida y obtiene datos del perfume via FeignClient). " +
                      "Puerto: 8082"
    )
)
@Configuration
public class OpenApiConfig {

}
