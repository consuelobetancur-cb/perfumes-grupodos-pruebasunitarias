package com.grupodos.carrito.controller;

import com.grupodos.carrito.dto.CarritoResponseDTO;
import com.grupodos.carrito.dto.ItemCarritoRequestDTO;
import com.grupodos.carrito.service.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carritos")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    // POST http://localhost:8081/api/carritos?usuario=Andres
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestParam String usuario) {
        CarritoResponseDTO nuevoCarrito = carritoService.crear(usuario);
        
        // Creamos la respuesta personalizada en JSON
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Carrito creado exitosamente");
        respuesta.put("carrito", nuevoCarrito);
        
        return ResponseEntity.status(201).body(respuesta);
    }

    // GET http://localhost:8081/api/carritos/1
    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return carritoService.obtenerPorId(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET http://localhost:8081/api/carritos?usuario=Andres
    @GetMapping
    public ResponseEntity<List<CarritoResponseDTO>> obtenerPorUsuario(@RequestParam String usuario) {
        return ResponseEntity.ok(carritoService.obtenerPorUsuario(usuario));
    }

    // POST http://localhost:8081/api/carritos/1/items
    @PostMapping("/{id}/items")
    public ResponseEntity<CarritoResponseDTO> agregarItem(@PathVariable Long id,
                                                          @Valid @RequestBody ItemCarritoRequestDTO dto) {
        return ResponseEntity.ok(carritoService.agregarItem(id, dto));
    }

    // DELETE http://localhost:8081/api/carritos/1/items/5
    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<CarritoResponseDTO> quitarItem(@PathVariable Long id,
                                                         @PathVariable Long itemId) {
        return ResponseEntity.ok(carritoService.quitarItem(id, itemId));
    }

    // DELETE http://localhost:8081/api/carritos/1/vaciar
    @DeleteMapping("/{id}/vaciar")
    public ResponseEntity<CarritoResponseDTO> vaciar(@PathVariable Long id) {
        return ResponseEntity.ok(carritoService.vaciar(id));
    }

    // DELETE http://localhost:8081/api/carritos/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        if (carritoService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        carritoService.eliminar(id);
        
        // Creamos la respuesta personalizada en JSON
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Carrito borrado correctamente");
        
        return ResponseEntity.ok(respuesta);
    }
}