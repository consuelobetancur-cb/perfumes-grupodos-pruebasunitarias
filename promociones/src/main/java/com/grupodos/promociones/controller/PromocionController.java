package com.grupodos.promociones.controller;

import com.grupodos.promociones.dto.AplicarDescuentoDTO;
import com.grupodos.promociones.dto.PromocionRequestDTO;
import com.grupodos.promociones.model.Promocion;
import com.grupodos.promociones.service.PromocionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promociones")
@RequiredArgsConstructor
public class PromocionController {

    private final PromocionService promocionService;

    // POST http://localhost:8089/api/promociones
    @PostMapping
    public ResponseEntity<Promocion> crear(@Valid @RequestBody PromocionRequestDTO dto) {
        return ResponseEntity.status(201).body(promocionService.crear(dto));
    }

    // GET http://localhost:8089/api/promociones
    @GetMapping
    public ResponseEntity<List<Promocion>> obtenerTodas() {
        return ResponseEntity.ok(promocionService.obtenerTodas());
    }

    // GET http://localhost:8089/api/promociones/validar?codigo=PERFUME20&total=50000
    @GetMapping("/validar")
    public ResponseEntity<AplicarDescuentoDTO> validarCupon(
            @RequestParam String codigo, 
            @RequestParam Double total) {
        return ResponseEntity.ok(promocionService.calcularDescuento(codigo, total));
    }
}