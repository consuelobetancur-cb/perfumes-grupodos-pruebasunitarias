package com.grupodos.perfumes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.grupodos.perfumes.model.Categoria;
import com.grupodos.perfumes.repository.CategoriaRepository;
import com.grupodos.perfumes.repository.PerfumeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final PerfumeRepository perfumeRepository;

    public List<Categoria> obtenerTodas() { return categoriaRepository.findAll(); }
    public Optional<Categoria> obtenerPorId(Long id) { return categoriaRepository.findById(id); }
    public Categoria guardar(Categoria c) { return categoriaRepository.save(c); }
    
    public void eliminar(Long id) {
        
        if (perfumeRepository.existsByCategoriaId(id)) {
            throw new IllegalStateException("No se puede eliminar la categoría porque existen perfumes vinculados a ella.");
        }
        categoriaRepository.deleteById(id);
    }
}