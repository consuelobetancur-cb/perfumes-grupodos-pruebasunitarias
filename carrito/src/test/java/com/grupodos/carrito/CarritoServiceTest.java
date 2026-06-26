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

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    @DisplayName("agregarItem: lanza excepcion si el carrito no existe")
    void agregarItem_carritoNoExiste_lanzaExcepcion() {
        ItemCarritoRequestDTO request = TestDataFactory.unItemRequest(123L);
        
       
        when(carritoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> carritoService.agregarItem(1L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("El carrito con id 1 no existe");

        
        verifyNoInteractions(catalogoClient);
        verify(carritoRepository, never()).save(any(Carrito.class));
    }

    @Test
    @DisplayName("agregarItem: lanza excepcion si el perfume no existe en el catalogo")
    void agregarItem_perfumeNoExisteEnCatalogo_lanzaExcepcion() {
        Carrito carrito = TestDataFactory.unCarrito();
        ItemCarritoRequestDTO request = TestDataFactory.unItemRequest(999L);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        
        
        FeignException.NotFound mockNotFound = mock(FeignException.NotFound.class);
        when(catalogoClient.obtenerPerfume(999L)).thenThrow(mockNotFound);

        assertThatThrownBy(() -> carritoService.agregarItem(1L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("El perfume con id 999 no existe en el catálogo");

        verify(carritoRepository, never()).save(any(Carrito.class));
    }

    @Test
    @DisplayName("agregarItem: agrega un perfume nuevo al carrito de manera exitosa")
    void agregarItem_productoNuevo_agregaExitosamente() {
        Carrito carrito = TestDataFactory.unCarrito();
        PerfumeDTO perfume = TestDataFactory.unPerfumeDTO();
        ItemCarritoRequestDTO request = TestDataFactory.unItemRequest(perfume.getId());

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(catalogoClient.obtenerPerfume(perfume.getId())).thenReturn(perfume);
        
        
        when(carritoRepository.save(any(Carrito.class))).thenAnswer(inv -> inv.getArgument(0));

        CarritoResponseDTO respuesta = carritoService.agregarItem(1L, request);

      
        assertThat(respuesta).isNotNull();
        assertThat(respuesta.getItems()).hasSize(1);
        assertThat(respuesta.getItems().get(0).getNombrePerfume()).isEqualTo(perfume.getNombre());
        assertThat(respuesta.getItems().get(0).getCantidad()).isEqualTo(request.getCantidad());

        
        BigDecimal subtotalEsperado = perfume.getPrecio().multiply(BigDecimal.valueOf(request.getCantidad()));
        assertThat(respuesta.getTotal()).isEqualByComparingTo(subtotalEsperado);

        verify(carritoRepository).save(carrito);
    }

    @Test
    @DisplayName("agregarItem: si el perfume ya existe, acumula la cantidad en lugar de duplicar fila")
    void agregarItem_productoExistente_acumulaCantidad() {
        Carrito carrito = TestDataFactory.unCarrito();
        PerfumeDTO perfume = TestDataFactory.unPerfumeDTO();
      
        ItemCarrito itemExistente = TestDataFactory.unItem(perfume, carrito);
        itemExistente.setCantidad(2);
        carrito.getItems().add(itemExistente);

        ItemCarritoRequestDTO request = new ItemCarritoRequestDTO();
        request.setPerfumeId(perfume.getId());
        request.setCantidad(3);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(catalogoClient.obtenerPerfume(perfume.getId())).thenReturn(perfume);
        when(carritoRepository.save(any(Carrito.class))).thenAnswer(inv -> inv.getArgument(0));

        CarritoResponseDTO respuesta = carritoService.agregarItem(1L, request);

        assertThat(respuesta.getItems()).hasSize(1);
        assertThat(respuesta.getItems().get(0).getCantidad()).isEqualTo(5);

        BigDecimal totalEsperado = perfume.getPrecio().multiply(BigDecimal.valueOf(5));
        assertThat(respuesta.getTotal()).isEqualByComparingTo(totalEsperado);
    }

    @Test
    @DisplayName("quitarItem: remueve un item especifico del carrito")
    void quitarItem_remueveItemCorrectamente() {
        Carrito carrito = TestDataFactory.unCarrito();
        PerfumeDTO perfume = TestDataFactory.unPerfumeDTO();
        ItemCarrito item = TestDataFactory.unItem(perfume, carrito);
        item.setId(5L);
        carrito.getItems().add(item);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(itemCarritoRepository.findById(5L)).thenReturn(Optional.of(item));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        CarritoResponseDTO respuesta = carritoService.quitarItem(1L, 5L);

        assertThat(respuesta.getItems()).isEmpty();
        assertThat(respuesta.getTotal()).isEqualByComparingTo(BigDecimal.ZERO);
        verify(carritoRepository).save(carrito);
    }

    @Test
    @DisplayName("vaciar: elimina todos los productos de golpe")
    void vaciar_limpiaTodoElCarrito() {
        Carrito carrito = TestDataFactory.unCarrito();
        
        // Agregamos dos perfumes distintos para simular un carrito con volumen
        PerfumeDTO p1 = TestDataFactory.unPerfumeDTO();
        PerfumeDTO p2 = TestDataFactory.unPerfumeDTO();
        carrito.getItems().add(TestDataFactory.unItem(p1, carrito));
        carrito.getItems().add(TestDataFactory.unItem(p2, carrito));

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        CarritoResponseDTO respuesta = carritoService.vaciar(1L);

        assertThat(respuesta.getItems()).isEmpty();
        assertThat(respuesta.getTotal()).isEqualByComparingTo(BigDecimal.ZERO);
        verify(carritoRepository).save(carrito);
    }
}