package com.grupodos.carrito;

import com.grupodos.carrito.client.CatalogoClient;
import com.grupodos.carrito.dto.CarritoResponseDTO;
import com.grupodos.carrito.dto.ItemCarritoRequestDTO;
import com.grupodos.carrito.dto.PerfumeDTO;
import com.grupodos.carrito.model.Carrito;
import com.grupodos.carrito.model.ItemCarrito;
import com.grupodos.carrito.repository.CarritoRepository;
import com.grupodos.carrito.repository.ItemCarritoRepository;
import com.grupodos.carrito.service.CarritoService;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CarritoService - Pruebas Unitarias de Perfumes")
public class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private ItemCarritoRepository itemCarritoRepository;

    @Mock
    private CatalogoClient catalogoClient;

    @InjectMocks
    private CarritoService carritoService;

    @Test
    @DisplayName("crear: guarda y retorna el nuevo carrito exitosamente")
    void crear_Exitoso() {
        Carrito carritoGuardado = TestDataFactory.unCarrito();
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carritoGuardado);

        CarritoResponseDTO resultado = carritoService.crear(carritoGuardado.getUsuario());

        assertThat(resultado).isNotNull();
        assertThat(resultado.getUsuario()).isEqualTo(carritoGuardado.getUsuario());
        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    @DisplayName("agregarItem: lanza excepción si el carrito no existe")
    void agregarItem_carritoNoEncontrado_lanzaExcepcion() {
        ItemCarritoRequestDTO request = TestDataFactory.unItemRequest(10L);
        when(carritoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> carritoService.agregarItem(1L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Carrito no encontrado: 1");

        verify(carritoRepository, never()).save(any());
    }

    @Test
    @DisplayName("agregarItem: lanza excepción si el perfume no existe en catálogo")
    void agregarItem_perfumeNoExiste_lanzaExcepcion() {
        Carrito carrito = TestDataFactory.unCarrito();
        ItemCarritoRequestDTO request = TestDataFactory.unItemRequest(99L);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(catalogoClient.obtenerPerfume(99L)).thenThrow(mock(FeignException.NotFound.class));

        assertThatThrownBy(() -> carritoService.agregarItem(1L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("El perfume con id 99 no existe en el catálogo");
    }

    @Test
    @DisplayName("agregarItem: añade un nuevo perfume al carrito de forma exitosa")
    void agregarItem_nuevoPerfume_agregaCorrectamente() {
        Carrito carrito = TestDataFactory.unCarrito();
        PerfumeDTO perfume = TestDataFactory.unPerfumeDTO();
        ItemCarritoRequestDTO request = new ItemCarritoRequestDTO();
        request.setPerfumeId(perfume.getId());
        request.setCantidad(2);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(catalogoClient.obtenerPerfume(perfume.getId())).thenReturn(perfume);
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        CarritoResponseDTO resultado = carritoService.agregarItem(1L, request);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getItems()).hasSize(1);
        assertThat(resultado.getItems().get(0).getPerfumeId()).isEqualTo(perfume.getId());
        verify(carritoRepository).save(carrito);
    }

    @Test
    @DisplayName("agregarItem: incrementa la cantidad si el perfume ya estaba en el carrito")
    void agregarItem_perfumeExistente_incrementaCantidad() {
        Carrito carrito = TestDataFactory.unCarrito();
        PerfumeDTO perfume = TestDataFactory.unPerfumeDTO();
        ItemCarrito itemExistente = TestDataFactory.unItem(perfume, carrito);
        itemExistente.setCantidad(1);
        carrito.getItems().add(itemExistente);

        ItemCarritoRequestDTO request = new ItemCarritoRequestDTO();
        request.setPerfumeId(perfume.getId());
        request.setCantidad(3);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(catalogoClient.obtenerPerfume(perfume.getId())).thenReturn(perfume);
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        CarritoResponseDTO resultado = carritoService.agregarItem(1L, request);

        assertThat(resultado.getItems()).hasSize(1);
        assertThat(itemExistente.getCantidad()).isEqualTo(4);
    }

    @Test
    @DisplayName("quitarItem: remueve un item de forma exitosa si pertenece al carrito")
    void quitarItem_itemPertenece_remueveCorrectamente() {
        Carrito carrito = TestDataFactory.unCarrito();
        carrito.setId(1L); // Para que coincida con la validación del ID
        
        PerfumeDTO perfume = TestDataFactory.unPerfumeDTO();
        ItemCarrito item = TestDataFactory.unItem(perfume, carrito);
        item.setId(5L);
        carrito.getItems().add(item);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(itemCarritoRepository.findById(5L)).thenReturn(Optional.of(item));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        CarritoResponseDTO resultado = carritoService.quitarItem(1L, 5L);

        assertThat(resultado.getItems()).isEmpty();
        verify(carritoRepository).save(carrito);
    }

    @Test
    @DisplayName("quitarItem: lanza excepción si el item pertenece a otro carrito")
    void quitarItem_itemDeOtroCarrito_lanzaExcepcion() {
        Carrito carritoActual = TestDataFactory.unCarrito();
        carritoActual.setId(1L);

        Carrito otroCarrito = TestDataFactory.unCarrito();
        otroCarrito.setId(2L); // ID diferente

        PerfumeDTO perfume = TestDataFactory.unPerfumeDTO();
        ItemCarrito item = TestDataFactory.unItem(perfume, otroCarrito); // Amarrado al otro carrito
        item.setId(5L);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carritoActual));
        when(itemCarritoRepository.findById(5L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> carritoService.quitarItem(1L, 5L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no pertenece al carrito 1");

        verify(carritoRepository, never()).save(any());
    }

    @Test
    @DisplayName("vaciar: limpia todos los elementos del carrito")
    void vaciar_limpiaTodosLosItems() {
        Carrito carrito = TestDataFactory.unCarrito();
        PerfumeDTO perfume = TestDataFactory.unPerfumeDTO();
        carrito.getItems().add(TestDataFactory.unItem(perfume, carrito));

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        CarritoResponseDTO resultado = carritoService.vaciar(1L);

        assertThat(resultado.getItems()).isEmpty();
        verify(carritoRepository).save(carrito);
    }
}