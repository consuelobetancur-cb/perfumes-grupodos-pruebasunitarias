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

import jakarta.transaction.Transactional;
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

   
    @Transactional
    public PerfumeResponseDTO actualizar(Long id, PerfumeRequestDTO dto) {

        Perfume perfume = perfumerepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfume no encontrado con ID: " + id));
        perfume.setNombre(dto.getNombre());
        perfume.setMarca(dto.getMarca());
        perfume.setPrecio(dto.getPrecio());
        perfume.setStock(dto.getStock());


        if (!perfume.getCategoria().getId().equals(dto.getCategoriaId())) {
            Categoria nuevaCategoria = categoriarepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Nueva categoría no encontrada: " + dto.getCategoriaId()));
            perfume.setCategoria(nuevaCategoria);
        }

        return mapToDTO(perfumerepository.save(perfume));
    }

    public void eliminar(Long id) {
        perfumerepository.deleteById(id);
    }

   private PerfumeResponseDTO mapToDTO(Perfume l) {

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
