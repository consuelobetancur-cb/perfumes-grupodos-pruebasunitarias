package com.grupodos.resena;

import com.grupodos.resena.TestDataFactory;
import com.grupodos.resena.client.CatalogoClient;
import com.grupodos.resena.dto.PerfumeDTO;
import com.grupodos.resena.dto.ResenaRequestDTO;
import com.grupodos.resena.model.Resena;
import com.grupodos.resena.repository.ResenaRepository;
import com.grupodos.resena.service.ResenaService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResenaServiceTest {

    
    @Mock 
    private ResenaRepository repository;
    @Mock 
    private CatalogoClient client;
    @InjectMocks 
    private ResenaService service;
    @Test
    void crear_resenaValida_retornaResponse() {
        ResenaRequestDTO dto = TestDataFactory.unResenaRequest();
        
        PerfumeDTO pDto = new PerfumeDTO(1L, "Test", "Marca", BigDecimal.TEN, "Cat");
        
        when(client.obtenerPerfume(1L)).thenReturn(pDto);
        when(repository.save(any(Resena.class))).thenReturn(TestDataFactory.unaResena());

        var resultado = service.crear(dto);

        assertThat(resultado.getCalificacion()).isEqualTo(5);
    }
@Test
    @org.junit.jupiter.api.DisplayName("crear: lanza error si el usuario ya tiene reseña para ese perfume")
    void crear_resenaDuplicada_lanzaExcepcion() {
        ResenaRequestDTO dto = TestDataFactory.unResenaRequest();
        when(repository.existsByPerfumeIdAndUsuario(dto.getPerfumeId(), dto.getUsuario())).thenReturn(true);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.crear(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ya tiene una reseña");
        
        verify(repository, never()).save(any(Resena.class));
    }

    @Test
    @org.junit.jupiter.api.DisplayName("obtenerTodas: retorna la lista completa")
    void obtenerTodas_retornaLista() {
        when(repository.findAll()).thenReturn(java.util.List.of(TestDataFactory.unaResena()));
        var resultado = service.obtenerTodas();

        org.assertj.core.api.Assertions.assertThat(resultado).isNotEmpty();
        verify(repository).findAll();
    }

    @Test
    @org.junit.jupiter.api.DisplayName("obtenerPorId: retorna la reseña si existe")
    void obtenerPorId_existe_retornaResena() {

        Resena resena = TestDataFactory.unaResena();
        when(repository.findById(resena.getId())).thenReturn(java.util.Optional.of(resena));

        var resultado = service.obtenerPorId(resena.getId());

        org.assertj.core.api.Assertions.assertThat(resultado).isPresent();
        verify(repository).findById(resena.getId());
    }

    @Test
    @org.junit.jupiter.api.DisplayName("obtenerPromedio: calcula correctamente el promedio del perfume")
    void obtenerPromedio_calculaCorrectamente() {
        Long perfumeId = 1L;
        PerfumeDTO pDto = new PerfumeDTO(perfumeId, "Test", "Marca", java.math.BigDecimal.TEN, "Cat");
        
        when(client.obtenerPerfume(perfumeId)).thenReturn(pDto);
        when(repository.promedioCalificacion(perfumeId)).thenReturn(4.5);
        when(repository.findByPerfumeId(perfumeId)).thenReturn(java.util.List.of(TestDataFactory.unaResena(), TestDataFactory.unaResena()));

        var resultado = service.obtenerPromedio(perfumeId);

        org.assertj.core.api.Assertions.assertThat(resultado.getPromedioCalificacion()).isEqualTo(4.5);
        org.assertj.core.api.Assertions.assertThat(resultado.getTotalResenas()).isEqualTo(2);
    }

    @Test
    @org.junit.jupiter.api.DisplayName("eliminar: borra la reseña si existe")
    void eliminar_existe_eliminaExitosamente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @org.junit.jupiter.api.DisplayName("eliminar: lanza error si no encuentra la reseña")
    void eliminar_noExiste_lanzaExcepcion() {
        when(repository.existsById(99L)).thenReturn(false);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.eliminar(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no encontrada");
    }
}