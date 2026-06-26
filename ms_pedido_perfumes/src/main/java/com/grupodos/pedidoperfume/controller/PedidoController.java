package com.grupodos.pedidoperfume.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupodos.pedidoperfume.dto.PedidoRequestDTO;
import com.grupodos.pedidoperfume.dto.PedidoResponseDTO;
import com.grupodos.pedidoperfume.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Pedidos",
    description = "API para gestion de pedidos de perfumes"
)
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService pedidoService;

    
     @Operation(
        summary = "Crear pedido",
        description = "Valida el cliente en codigoms-usuarios y el perfume en codigoms-catalogo via FeignClient."
    )
    @PostMapping // http://localhost:8082/api/pedidos
    public ResponseEntity<PedidoResponseDTO> crear(@Valid @RequestBody PedidoRequestDTO dto) {
        return ResponseEntity.status(201).body(pedidoService.crear(dto));
    }


    @Operation(summary = "Listar todos los pedidos")
    @GetMapping // http://localhost:8082/api/pedidos
    public ResponseEntity<List<PedidoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(pedidoService.obtenerTodos());
    }

    @Operation(summary = "Obtener pedido por ID")
    @GetMapping("/{id}") // http://localhost:8082/api/pedidos/{id}
    public ResponseEntity<PedidoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar pedidos de un cliente")
    @GetMapping("/cliente/{cliente}") // http://localhost:8082/api/pedidos/cliente/{cliente}
    public ResponseEntity<List<PedidoResponseDTO>> obtenerPorCliente(
            @Parameter(example = "carlos") @PathVariable String cliente) {
        return ResponseEntity.ok(pedidoService.obtenerPorCliente(cliente));
    }

    @Operation(summary = "Eliminar pedido")
    @DeleteMapping("/{id}") // http://localhost:8082/api/pedidos/{id}
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (pedidoService.obtenerPorId(id).isEmpty()) return ResponseEntity.notFound().build();
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
