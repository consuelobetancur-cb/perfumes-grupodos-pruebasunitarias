package com.grupodos.pagos.controller;

import com.grupodos.pagos.dto.PagoRequestDTO;
import com.grupodos.pagos.dto.PagoResponseDTO;
import com.grupodos.pagos.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pagos", description = "Registro y validación de pagos para Perfulandia SPA")
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;
    // Método: GET
    // URL: http://localhost:8085/api/pagos
    @Operation(summary = "Listar todos los registros de pago")
    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }
    // Método: GET
    // Ruta: http://localhost:8085/api/pagos/1
    @Operation(summary = "Obtener un pago por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return pagoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // Método: GET
    // Ruta: http://localhost:8085/api/pagos/pedido/1
    @Operation(summary = "Buscar el historial de pagos de un pedido específico")
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<PagoResponseDTO>> buscarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pagoService.buscarPorPedido(pedidoId));
    }
    // Método: POST
    // URL: http://localhost:8085/api/pagos
    @Operation(summary = "Registrar un nuevo intento de pago")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PagoResponseDTO crear(@Valid @RequestBody PagoRequestDTO dto) {
        return pagoService.crear(dto);
    }
    // Método: PATCH
    // Ruta: http://localhost:8085/api/pagos/{id}/estado?estado=COMPLETADO
    @Operation(summary = "Actualizar el estado de un pago (Ej: COMPLETADO o RECHAZADO)")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PagoResponseDTO> actualizarEstado(
            @PathVariable Long id, 
            @RequestParam String estado) {
        return ResponseEntity.ok(pagoService.actualizarEstado(id, estado));
    }
}