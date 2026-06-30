package com.grupodos.pagos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "API de Pagos - Perfulandia SPA",
                version = "1.0",
                description = "Microservicio encargado del registro y control de transacciones de la tienda Perfulandia. " +
                              "Permite verificar historiales de pago asociados a un pedido específico. " +
                              "Endpoint clave para integración: GET /api/pagos/pedido/{pedidoId}"
        )
)
@Configuration
public class OpenAPIConfig {}