package com.grupodos.favorito.config;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@OpenAPIDefinition(
info = @Info(
        title = "ms-favorito-perfume",
        version = "1.0",
        description = "Gestiona la lista de perfumes favoritos por usuario. " +
                      "A diferencia de carrito y pedidos, usa WebClient (reactivo) en lugar de FeignClient (declarativo) " +
                      "para comunicarse con perfulandia-ms-usuarios y perfulandia-ms-catalogo. " +
                      "Puerto: 8085"
    )
)
@Configuration
public class OpenApiConfig {

}
