package com.grupodos.perfumes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupodos.perfumes.dto.PerfumeRequestDTO;
import com.grupodos.perfumes.dto.PerfumeResponseDTO;
import com.grupodos.perfumes.service.PerfumeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/perfumes")
@RequiredArgsConstructor
public class PerfumeController {
    private final PerfumeService perfumeService;

    @GetMapping // http://localhost:8083/api/perfumes
    public ResponseEntity<List<PerfumeResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(perfumeService.obtenerTodos());
    }

    @GetMapping("/{id}") // http://localhost:8083/api/perfumes/{id}
    public ResponseEntity<PerfumeResponseDTO> obtenerPorId(@PathVariable Long id) {
        return perfumeService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping // http://localhost:8083/api/perfumes
    public ResponseEntity<PerfumeResponseDTO> crear(@Valid @RequestBody PerfumeRequestDTO dto) {
        return ResponseEntity.status(201).body(perfumeService.guardar(dto));
    }

    @PutMapping("/{id}") // http://localhost:8083/api/perfumes/{id}
    public ResponseEntity<PerfumeResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody PerfumeRequestDTO dto) {
        // Buscamos si existe antes de actualizar
        if (perfumeService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(perfumeService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}") //http://localhost:8083/api/perfumes/{id}
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
    if (perfumeService.obtenerPorId(id).isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Perfume no encontrado.");
    }
    perfumeService.eliminar(id);
    return ResponseEntity.ok("Éxito: El perfume con ID " + id + " fue eliminado correctamente.");
}

}
