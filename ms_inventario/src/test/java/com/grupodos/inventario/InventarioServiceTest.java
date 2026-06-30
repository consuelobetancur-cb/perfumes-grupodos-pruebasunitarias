package com.grupodos.inventario;

import com.grupodos.inventario.dto.InventarioRequestDTO;
import com.grupodos.inventario.dto.InventarioResponseDTO;
import com.grupodos.inventario.model.Inventario;
import com.grupodos.inventario.repository.InventarioRepository;
import com.grupodos.inventario.service.InventarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InventarioService - Pruebas Unitarias")
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    @DisplayName("obtenerTodos: retorna todo el inventario de todas las sucursales")
    void obtenerTodos_retornaListaCompleta() {
        Inventario i1 = TestDataFactory.unInventario();
        Inventario i2 = TestDataFactory.unInventario();
        when(inventarioRepository.findAll()).thenReturn(List.of(i1, i2));

        List<InventarioResponseDTO> resultado = inventarioService.obtenerTodos();

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(InventarioResponseDTO::getSucursal)
                .containsExactly(i1.getSucursal(), i2.getSucursal());
        verify(inventarioRepository).findAll();
    }

    @Test
    @DisplayName("obtenerPorId: retorna el registro de inventario cuando existe")
    void obtenerPorId_inventarioExiste_retornaDTO() {
        Inventario inventario = TestDataFactory.unInventario();
        when(inventarioRepository.findById(inventario.getId())).thenReturn(Optional.of(inventario));

        Optional<InventarioResponseDTO> resultado = inventarioService.obtenerPorId(inventario.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getSucursal()).isEqualTo(inventario.getSucursal());
        assertThat(resultado.get().getCantidad()).isEqualTo(inventario.getCantidad());
        verify(inventarioRepository).findById(inventario.getId());
    }

    @Test
    @DisplayName("buscarPorPerfume: retorna lista de stock en diferentes sucursales para un perfume")
    void buscarPorPerfume_encontrado_retornaListaDTO() {
        Inventario inventario = TestDataFactory.unInventario();
        when(inventarioRepository.findByPerfumeId(inventario.getPerfumeId())).thenReturn(List.of(inventario));

        List<InventarioResponseDTO> resultado = inventarioService.buscarPorPerfume(inventario.getPerfumeId());

        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getPerfumeId()).isEqualTo(inventario.getPerfumeId());
        verify(inventarioRepository).findByPerfumeId(inventario.getPerfumeId());
    }

    @Test
    @DisplayName("buscarPorSucursal: retorna lista de todos los perfumes en una sucursal")
    void buscarPorSucursal_encontrado_retornaListaDTO() {
        Inventario inventario = TestDataFactory.unInventario();
        when(inventarioRepository.findBySucursal(inventario.getSucursal())).thenReturn(List.of(inventario));

        List<InventarioResponseDTO> resultado = inventarioService.buscarPorSucursal(inventario.getSucursal());

        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getSucursal()).isEqualTo(inventario.getSucursal());
        verify(inventarioRepository).findBySucursal(inventario.getSucursal());
    }

    @Test
    @DisplayName("guardar (Upsert - Insert): crea un nuevo registro si el perfume no existe en la sucursal")
    void guardar_registroNoExiste_creaNuevoYRetornaDTO() {
        InventarioRequestDTO request = TestDataFactory.unInventarioRequest();

        Inventario inventarioGuardado = new Inventario();
        inventarioGuardado.setId(1L);
        inventarioGuardado.setPerfumeId(request.getPerfumeId());
        inventarioGuardado.setSucursal(request.getSucursal());
        inventarioGuardado.setCantidad(request.getCantidad());
        inventarioGuardado.setFechaActualizacion(LocalDateTime.now());
        when(inventarioRepository.findByPerfumeIdAndSucursal(request.getPerfumeId(), request.getSucursal()))
                .thenReturn(Optional.empty());
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioGuardado);
        InventarioResponseDTO resultado = inventarioService.guardar(request);
        assertThat(resultado.getPerfumeId()).isEqualTo(request.getPerfumeId());
        assertThat(resultado.getSucursal()).isEqualTo(request.getSucursal());
        verify(inventarioRepository).findByPerfumeIdAndSucursal(request.getPerfumeId(), request.getSucursal());
        verify(inventarioRepository).save(any(Inventario.class));
    }
    @Test
    @DisplayName("guardar (Upsert - Update): actualiza la cantidad si el perfume ya existe en la sucursal")
    void guardar_registroYaExiste_actualizaCantidadYRetornaDTO() {
        InventarioRequestDTO request = TestDataFactory.unInventarioRequest();
        Inventario inventarioExistente = new Inventario();
        inventarioExistente.setId(1L);
        inventarioExistente.setPerfumeId(request.getPerfumeId());
        inventarioExistente.setSucursal(request.getSucursal());
        inventarioExistente.setCantidad(5); 
        inventarioExistente.setFechaActualizacion(LocalDateTime.now().minusDays(1));

        Inventario inventarioActualizado = new Inventario();
        inventarioActualizado.setId(1L);
        inventarioActualizado.setPerfumeId(request.getPerfumeId());
        inventarioActualizado.setSucursal(request.getSucursal());
        inventarioActualizado.setCantidad(request.getCantidad()); 
        inventarioActualizado.setFechaActualizacion(LocalDateTime.now());

        when(inventarioRepository.findByPerfumeIdAndSucursal(request.getPerfumeId(), request.getSucursal()))
                .thenReturn(Optional.of(inventarioExistente));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioActualizado);

        InventarioResponseDTO resultado = inventarioService.guardar(request);
        assertThat(resultado.getCantidad()).isEqualTo(request.getCantidad());
        verify(inventarioRepository).findByPerfumeIdAndSucursal(request.getPerfumeId(), request.getSucursal());
        verify(inventarioRepository).save(any(Inventario.class));
    }
}