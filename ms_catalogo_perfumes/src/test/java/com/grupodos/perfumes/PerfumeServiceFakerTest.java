package com.grupodos.perfumes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupodos.perfumes.dto.PerfumeRequestDTO;
import com.grupodos.perfumes.dto.PerfumeResponseDTO;
import com.grupodos.perfumes.model.Categoria;
import com.grupodos.perfumes.model.Perfume;
import com.grupodos.perfumes.repository.CategoriaRepository;
import com.grupodos.perfumes.repository.PerfumeRepository;
import com.grupodos.perfumes.service.PerfumeService;

@ExtendWith(MockitoExtension.class)
class PerfumeServiceFakerTest {
    @Mock
    private PerfumeRepository perfumeRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private PerfumeService perfumeService;

    @Test
    void obtenerPorId_cuandoPerfumeExiste_devuelveDTO() {
        Categoria cat = TestDataFactory.unaCategoria();
        Perfume perfume = TestDataFactory.unPerfume(cat);
        when(perfumeRepository.findById(perfume.getId())).thenReturn(Optional.of(perfume));

        Optional<PerfumeResponseDTO> resultado = perfumeService.obtenerPorId(perfume.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo(perfume.getNombre());
        assertThat(resultado.get().getPrecio()).isEqualByComparingTo(perfume.getPrecio());
    }

    @RepeatedTest(5)
    void guardar_siempreGeneraDatosValidos() {
        Categoria cat = TestDataFactory.unaCategoria();
        PerfumeRequestDTO request = TestDataFactory.unPerfumeRequest(cat.getId());
        Perfume guardado = TestDataFactory.unPerfume(cat);

        when(categoriaRepository.findById(cat.getId())).thenReturn(Optional.of(cat));
        when(perfumeRepository.save(any(Perfume.class))).thenReturn(guardado);

        PerfumeResponseDTO resultado = perfumeService.guardar(request);

        assertThat(resultado.getId()).isPositive();
        assertThat(resultado.getNombre()).isNotBlank();
        assertThat(resultado.getPrecio()).isPositive();
        assertThat(resultado.getStock()).isPositive();
    }

    @Test
    void obtenerTodos_devuelveTodosLosPerfumesGenerados() {
        Categoria cat = TestDataFactory.unaCategoria();
        List<Perfume> perfumes = List.of(
                TestDataFactory.unPerfume(cat),
                TestDataFactory.unPerfume(cat),
                TestDataFactory.unPerfume(cat)
        );
        when(perfumeRepository.findAll()).thenReturn(perfumes);

        List<PerfumeResponseDTO> resultado = perfumeService.obtenerTodos();

        assertThat(resultado).hasSize(3);
        assertThat(resultado).allMatch(dto -> dto.getNombre() != null && !dto.getNombre().isBlank());
        assertThat(resultado).allMatch(dto -> dto.getPrecio().signum() > 0);
        assertThat(resultado).allMatch(dto -> dto.getStock() > 0);
    }
}
