package com.grupodos.inventario.controller;

import com.grupodos.inventario.dto.InventarioRequestDTO;
import com.grupodos.inventario.dto.InventarioResponseDTO;
import com.grupodos.inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Inventario", description = "Gestión de stock físico por sucursales para Perfulandia SPA")
@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {
    private final InventarioService inventarioService;
    // Método: GET
    // URL: http://localhost:8087/api/inventario
    @Operation(summary = "Listar todo el inventario global")
    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(inventarioService.obtenerTodos());
    }
    // Método: GET
    // Ruta: http://localhost:8087/api/inventario/1
    @Operation(summary = "Obtener registro de inventario por su ID único")
    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return inventarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // Método: GET
    // Ruta: http://localhost:8087/api/inventario/perfume/1
    @Operation(summary = "Buscar disponibilidad de un perfume específico en todas las sucursales")
    @GetMapping("/perfume/{perfumeId}")
    public ResponseEntity<List<InventarioResponseDTO>> buscarPorPerfume(@PathVariable Long perfumeId) {
        return ResponseEntity.ok(inventarioService.buscarPorPerfume(perfumeId));
    }
    // Método: GET
    // Ruta: http://localhost:8087/api/inventario/sucursal/Santiago
    @Operation(summary = "Listar todo el inventario perteneciente a una sucursal (Ej: Santiago)")
    @GetMapping("/sucursal/{sucursal}")
    public ResponseEntity<List<InventarioResponseDTO>> buscarPorSucursal(@PathVariable String sucursal) {
        return ResponseEntity.ok(inventarioService.buscarPorSucursal(sucursal));
    }
    // Método: POST
    // URL: http://localhost:8087/api/inventario
    @Operation(summary = "Registrar o actualizar el stock de un perfume en una sucursal")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventarioResponseDTO guardar(@Valid @RequestBody InventarioRequestDTO dto) {
        return inventarioService.guardar(dto);
    }
}