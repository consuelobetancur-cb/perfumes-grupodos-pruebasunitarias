package com.grupodos.favorito.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupodos.favorito.dto.FavoritoRequestDTO;
import com.grupodos.favorito.dto.FavoritoResponseDTO;
import com.grupodos.favorito.service.favoritoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Favoritos", description = "Controlador para gestionar la lista de perfumes favoritos por usuario. Usa WebClient reactivo en lugar de FeignClient para comunicarse con ms-catalogo-perfumes ") //perfulandia-ms-usuarios.")
@RestController
@RequestMapping("/api/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final  favoritoService FavoritoService;
    
    @Operation(
        summary = "Agregar perfume a favoritos")
        @PostMapping  // http://localhost:8086/api/favoritos
    
        public ResponseEntity<FavoritoResponseDTO> agregar(@Valid @RequestBody FavoritoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(FavoritoService.agregar(dto));
    }

    @Operation(summary = "Listar todos los favoritos")
    @GetMapping // http://localhost:8086/api/favoritos
    public ResponseEntity<List<FavoritoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(FavoritoService.listarTodos());
    }

    @Operation(summary = "Listar favoritos de un usuario")
    @GetMapping("/usuario/{usuario}") // http://localhost:8086/api/favoritos/usuario/{usuario}
    public ResponseEntity<List<FavoritoResponseDTO>> listarPorUsuario(
            @Parameter(example = "Perla") @PathVariable String usuario) {
        return ResponseEntity.ok(FavoritoService.listarPorUsuario(usuario));
    }

    @Operation(summary = "Ver que usuarios tienen un perfume en favoritos")
    @GetMapping("/perfume/{perfumeId}") // http://localhost:8086/api/favoritos/perfume/{perfumeId}
    public ResponseEntity<List<FavoritoResponseDTO>> listarPorPerfume(
            @Parameter(example = "1") @PathVariable Long perfumeId) {
        return ResponseEntity.ok(FavoritoService.listarPorPerfume(perfumeId));
    }

    @Operation(summary = "Eliminar favorito")
    @DeleteMapping("/{id}") // http://localhost:8086/api/favoritos/{id}
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        FavoritoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
