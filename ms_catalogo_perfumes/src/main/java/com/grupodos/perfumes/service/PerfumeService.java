package com.grupodos.perfumes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.grupodos.perfumes.dto.PerfumeRequestDTO;
import com.grupodos.perfumes.dto.PerfumeResponseDTO;
import com.grupodos.perfumes.model.Categoria;
import com.grupodos.perfumes.model.Perfume;
import com.grupodos.perfumes.repository.CategoriaRepository;
import com.grupodos.perfumes.repository.PerfumeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class PerfumeService {
    private final PerfumeRepository perfumerepository;
    private final CategoriaRepository categoriarepository;

    public List<PerfumeResponseDTO> obtenerTodos() {
        return perfumerepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public Optional<PerfumeResponseDTO> obtenerPorId(Long id) {
        log.info("Consulta de perfume Id{}", id);
        return perfumerepository.findById(id).map(this::mapToDTO);
    }

    public PerfumeResponseDTO guardar(PerfumeRequestDTO dto) {
        Categoria categoria = categoriarepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + dto.getCategoriaId()));
        Perfume perfume = new Perfume(null, dto.getNombre(), dto.getMarca(), dto.getPrecio(),dto.getStock(), categoria);
        return mapToDTO(perfumerepository.save(perfume));
    }

    public void eliminar(Long id) {
        perfumerepository.deleteById(id);
    }

   private PerfumeResponseDTO mapToDTO(Perfume l) {
    // Si la categoría existe, obtenemos el género, sino enviamos un valor por defecto
    String genero = (l.getCategoria() != null) ? l.getCategoria().getGenero() : "N/A";
    
    return new PerfumeResponseDTO(
            l.getId(), 
            l.getNombre(), 
            l.getMarca(),
            l.getPrecio(), 
            l.getStock(), 
            genero // Aquí solo pasas el género
    );
}
}
