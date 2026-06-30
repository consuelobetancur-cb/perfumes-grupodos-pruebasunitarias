package com.grupodos.usuarios.service;

import com.grupodos.usuarios.dto.UsuarioRequestDTO;
import com.grupodos.usuarios.dto.UsuarioResponseDTO;
import com.grupodos.usuarios.model.Usuario;
import com.grupodos.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<UsuarioResponseDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }
    public Optional<UsuarioResponseDTO> obtenerPorId(Long id) {
        return usuarioRepository.findById(id).map(this::toResponse);
    }
    public List<UsuarioResponseDTO> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre).stream()
                .map(this::toResponse)
                .toList();
    }
    public Optional<UsuarioResponseDTO> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).map(this::toResponse);
    }

    @Transactional
    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + dto.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccionEnvio(dto.getDireccionEnvio()); 
        usuario.setActivo(true);
        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario creado: {} (id={})", guardado.getNombre(), guardado.getId());
        return toResponse(guardado);
    }
    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con id " + id + " no encontrado"));
        usuarioRepository.delete(usuario);
        log.info("Usuario eliminado: id {}", id);
    }
    @Transactional
    public UsuarioResponseDTO desactivar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con id " + id + " no encontrado"));
        usuario.setActivo(false);
        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario desactivado: id {}", id);
        return toResponse(guardado);
    }
    private UsuarioResponseDTO toResponse(Usuario u) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setEmail(u.getEmail());
        dto.setTelefono(u.getTelefono());
        dto.setDireccionEnvio(u.getDireccionEnvio()); 
        dto.setActivo(u.getActivo());
        return dto;
    }
}