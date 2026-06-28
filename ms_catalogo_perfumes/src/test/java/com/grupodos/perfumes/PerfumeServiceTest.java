package com.grupodos.perfumes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
class PerfumeServiceTest {

    @Mock
    private PerfumeRepository perfumeRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private PerfumeService perfumeService;

    @Test
    void obtenerPorId_cuandoPerfumeExiste_devuelveDTO() {
    Categoria cat = new Categoria(1L, "Floral", "Fragancias frescas");
    
    Perfume perfume = new Perfume(
            1L, 
            "Chanel No. 5", 
            "Chanel", 
            new BigDecimal("150.00"), 
            10, 
            cat
    );
    
    when(perfumeRepository.findById(1L)).thenReturn(Optional.of(perfume));

    Optional<PerfumeResponseDTO> resultado = perfumeService.obtenerPorId(1L);

    // Then
    assertThat(resultado).isPresent();
    assertThat(resultado.get().getId()).isEqualTo(1L);
    assertThat(resultado.get().getNombre()).isEqualTo("Chanel No. 5");
    assertThat(resultado.get().getMarca()).isEqualTo("Chanel");
    assertThat(resultado.get().getPrecio()).isEqualByComparingTo("150.00");
    assertThat(resultado.get().getStock()).isEqualTo(10);
    
    // Verificamos que el mapeo de la categoría sea correcto
    assertThat(resultado.get().getCategoriaNombre()).isEqualTo("Floral");
}

    @Test
    void obtenerPorId_cuandoPerfumeNoExiste_devuelveOptionalVacio() {
        // Given
        when(perfumeRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<PerfumeResponseDTO> resultado = perfumeService.obtenerPorId(99L);

        // Then
        // Verificamos que el resultado sea un Optional vacío
        assertThat(resultado).isEmpty();
    }

    @Test
    void guardar_cuandoCategoriaExiste_retornaPerfumeGuardado() {
        // Given
        Categoria cat = new Categoria(2L, "Amaderado", "Fragancias intensas");

        PerfumeRequestDTO request = new PerfumeRequestDTO();
        request.setNombre("Bleu de Chanel");
        request.setMarca("Chanel");
        request.setPrecio(new BigDecimal("120000.00"));
        request.setStock(20);
        request.setCategoriaId(2L);

        Perfume perfumeGuardado = new Perfume(
                5L,
                request.getNombre(),
                request.getMarca(),
                request.getPrecio(),
                request.getStock(),
                cat
        );

        // Mockeamos la búsqueda de categoría y el guardado del perfume
        when(categoriaRepository.findById(2L)).thenReturn(Optional.of(cat));
        when(perfumeRepository.save(any(Perfume.class))).thenReturn(perfumeGuardado);

        // When
        PerfumeResponseDTO resultado = perfumeService.guardar(request);

        // Then
        assertThat(resultado.getId()).isEqualTo(5L);
        assertThat(resultado.getNombre()).isEqualTo("Bleu de Chanel");
        assertThat(resultado.getMarca()).isEqualTo("Chanel");
        assertThat(resultado.getCategoriaNombre()).isEqualTo("Amaderado");
        
        // Verificamos que el repositorio se llamó exactamente una vez
        verify(perfumeRepository, times(1)).save(any(Perfume.class));
    }
    
    @Test
    void guardar_cuandoCategoriaNoExiste_lanzaRuntimeException() {
        PerfumeRequestDTO request = new PerfumeRequestDTO();
        request.setNombre("Perfume Fantasma");
        request.setMarca("Marca Desconocida");
        request.setPrecio(new BigDecimal("99.00"));
        request.setStock(5);
        request.setCategoriaId(99L); // ID que no existe

        // Simulamos que el repositorio de categorías devuelve vacío
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        // Validamos que se lance la excepción y verifique el mensaje de error
        assertThatThrownBy(() -> perfumeService.guardar(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Categoria no encontrada con id: 99");
    }

    @Test
    void obtenerTodos_devuelveListaConTodosLosPerfumes() {
        // Given
        Categoria cat = new Categoria(1L, "Floral", "Fragancias frescas");
        List<Perfume> perfumes = List.of(
                new Perfume(1L, "Chanel No. 5", "Chanel", new BigDecimal("150000.00"), 10, cat),
                new Perfume(2L, "Miss Dior", "Dior", new BigDecimal("120000.00"), 5, cat)
        );
        when(perfumeRepository.findAll()).thenReturn(perfumes);

        // When
        List<PerfumeResponseDTO> resultado = perfumeService.obtenerTodos();

        // Then
        assertThat(resultado).hasSize(2);
        
        // Verificamos que los nombres coincidan en orden
        assertThat(resultado).extracting("nombre")
                .containsExactly("Chanel No. 5", "Miss Dior");
        
        // Verificamos opcionalmente que la categoría sea la correcta
        assertThat(resultado.get(0).getCategoriaNombre()).isEqualTo("Floral");
    }



}
