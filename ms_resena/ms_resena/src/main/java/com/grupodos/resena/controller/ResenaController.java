package com.grupodos.resena.controller;

import com.grupodos.resena.dto.ResenaPromedioDTO;
import com.grupodos.resena.dto.ResenaRequestDTO;
import com.grupodos.resena.dto.ResenaResponseDTO;
import com.grupodos.resena.service.ResenaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
public class ResenaController {
    private final ResenaService resenaService;
    @PostMapping
    public ResponseEntity<ResenaResponseDTO> crear(@Valid @RequestBody ResenaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resenaService.crear(dto));
    }
    @GetMapping
    public ResponseEntity<List<ResenaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(resenaService.obtenerTodas());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResenaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return resenaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/perfume/{perfumeId}")
    public ResponseEntity<List<ResenaResponseDTO>> obtenerPorPerfume(@PathVariable Long perfumeId) {
        return ResponseEntity.ok(resenaService.obtenerPorPerfume(perfumeId));
    }
    @GetMapping("/perfume/{perfumeId}/promedio")
    public ResponseEntity<ResenaPromedioDTO> obtenerPromedio(@PathVariable Long perfumeId) {
        return ResponseEntity.ok(resenaService.obtenerPromedio(perfumeId));
    }
    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<List<ResenaResponseDTO>> obtenerPorUsuario(@PathVariable String usuario) {
        return ResponseEntity.ok(resenaService.obtenerPorUsuario(usuario));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        resenaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}