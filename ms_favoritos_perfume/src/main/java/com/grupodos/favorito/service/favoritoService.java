package com.grupodos.favorito.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.grupodos.favorito.client.CatalogoClient;
import com.grupodos.favorito.client.UsuariosClient;
import com.grupodos.favorito.dto.FavoritoRequestDTO;
import com.grupodos.favorito.dto.FavoritoResponseDTO;
import com.grupodos.favorito.dto.PerfumeDTO;
import com.grupodos.favorito.model.Favorito;
import com.grupodos.favorito.repository.FavoritoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class favoritoService {
    private final FavoritoRepository favoritoRepository;
    private final CatalogoClient catalogoClient;
    private final UsuariosClient usuariosClient;


    @Transactional
    public FavoritoResponseDTO agregar(FavoritoRequestDTO dto) {

   usuariosClient.buscarPorNombre(dto.getUsuario())
        .orElseThrow(() -> new RuntimeException("El usuario '" + dto.getUsuario() + "' no existe..."));

    PerfumeDTO perfume = catalogoClient.buscarPerfume(dto.getPerfumeId())
            .orElseThrow(() -> new RuntimeException(
                    "El perfume con id " + dto.getPerfumeId() + " no existe en el catálogo de Perfulandia"));

    if (favoritoRepository.existsByUsuarioAndPerfumeId(dto.getUsuario(),dto.getPerfumeId() )) {
        throw new RuntimeException(
                "El usuario " + dto.getUsuario() + " ya tiene este perfume en favoritos");
    }

    Favorito favorito = new Favorito();
    favorito.setPerfumeId(dto.getPerfumeId());
    favorito.setNombrePerfume(perfume.getNombre());
    favorito.setUsuario(dto.getUsuario());
    favorito.setFechaAgregado(LocalDateTime.now());
    
     Favorito guardado = favoritoRepository.save(favorito);
        log.info("Favorito agregado: '{}' para usuario {}", perfume.getNombre(), dto.getUsuario());
        return toResponse(guardado);
}

        public List<FavoritoResponseDTO> listarTodos() {
                 return favoritoRepository.findAll().stream()
                .map(this::toResponse).toList();
    }

    public List<FavoritoResponseDTO> listarPorUsuario(String usuario) {
        return favoritoRepository.findByUsuario(usuario).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<FavoritoResponseDTO> listarPorPerfume(Long perfumeId) {
    return favoritoRepository.findByPerfumeId(perfumeId).stream()
            .map(this::toResponse)
            .toList();
}

@Transactional
public void eliminar(Long id) {
    Favorito favorito = favoritoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Favorito con id " + id + " no encontrado"));
    
    favoritoRepository.delete(favorito);
    
    log.info("Favorito eliminado: id {}", id);
}


    private FavoritoResponseDTO toResponse(Favorito favorito) {
        FavoritoResponseDTO dto = new FavoritoResponseDTO();
        dto.setId(favorito.getId());
        dto.setPerfumeId(favorito.getPerfumeId());
        dto.setNombre(favorito.getNombrePerfume());
        dto.setUsuario(favorito.getUsuario());
        dto.setFechaAgregado(favorito.getFechaAgregado());
        return dto;
    }
}
