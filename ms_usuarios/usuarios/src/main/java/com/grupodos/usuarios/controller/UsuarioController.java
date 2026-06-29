package com.grupodos.usuarios.controller;

import com.grupodos.usuarios.dto.UsuarioRequestDTO;
import com.grupodos.usuarios.dto.UsuarioResponseDTO;
import com.grupodos.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios", description = "Registro y gestión de usuarios para el sistema de Perfulandia SPA")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // GET http://localhost:8084/api/usuarios
    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    //GET http://localhost:8084/api/usuarios/1
    @Operation(summary = "Obtener usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //GET http://localhost:8084/api/usuarios/nombre/Héctor
    @Operation(
        summary = "Buscar usuarios por nombre", 
        description = "Devuelve una lista de usuarios que coincidan con el nombre proporcionado."
    )
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNombre(
            @Parameter(description = "Nombre del usuario", example = "Juan Perez") 
            @PathVariable String nombre) {
        return ResponseEntity.ok(usuarioService.buscarPorNombre(nombre));
    }

    //GET http://localhost:8084/api/usuarios/email/hector@gmail.com
    @Operation(
        summary = "Buscar usuario por email", 
        description = "Endpoint clave para validar identidad en el inicio de sesión o al realizar compras."
    )
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorEmail(
            @Parameter(description = "Correo electrónico exacto del usuario", example = "juan@mail.com") 
            @PathVariable String email) {
        return usuarioService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //POST http://localhost:8084/api/usuarios
    @Operation(summary = "Registrar un nuevo usuario (Cliente vía Web o Administrador)")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.crear(dto);
    }

    // PATCH http://localhost:8084/api/usuarios/1/desactivar
    @Operation(summary = "Desactivar usuario (Borrado lógico para el Administrador)")
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<UsuarioResponseDTO> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.desactivar(id));
    }

    // DELETE http://localhost:8084/api/usuarios/1
    @Operation(summary = "Eliminar usuario físicamente de la base de datos")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }
}

//Método: POST
//URL: http://localhost:8084/api/usuarios

//Método: POST
//URL: http://localhost:8084/api/usuarios

//Método: POST
//URL: http://localhost:8084/api/usuarios

//Método: PATCH
//URL: http://localhost:8084/api/usuarios/4/desactivar

//Método: DELETE
//URL: http://localhost:8084/api/usuarios/4