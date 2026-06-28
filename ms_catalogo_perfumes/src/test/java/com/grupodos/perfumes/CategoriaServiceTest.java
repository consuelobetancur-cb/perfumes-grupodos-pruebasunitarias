package com.grupodos.perfumes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupodos.perfumes.model.Categoria;
import com.grupodos.perfumes.repository.CategoriaRepository;
import com.grupodos.perfumes.repository.PerfumeRepository;
import com.grupodos.perfumes.service.CategoriaService;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {
    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private PerfumeRepository perfumeRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    @DisplayName("obtenerTodas: debe retornar lista de categorías")
    void obtenerTodas_retornaLista() {
        when(categoriaRepository.findAll()).thenReturn(List.of(new Categoria()));
        assertThat(categoriaService.obtenerTodas()).hasSize(1);
    }

    @Test
    @DisplayName("eliminar: lanza excepción si la categoría tiene perfumes vinculados")
    void eliminar_conPerfumes_lanzaExcepcion() {
        Long id = 1L;
        when(perfumeRepository.existsByCategoriaId(id)).thenReturn(true);

        assertThatThrownBy(() -> categoriaService.eliminar(id))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("existen perfumes vinculados");

        verify(categoriaRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("eliminar: ejecuta borrado si no hay perfumes vinculados")
    void eliminar_sinPerfumes_borraCorrectamente() {
        Long id = 1L;
        when(perfumeRepository.existsByCategoriaId(id)).thenReturn(false);

        categoriaService.eliminar(id);

        verify(categoriaRepository, times(1)).deleteById(id);
    }
}
