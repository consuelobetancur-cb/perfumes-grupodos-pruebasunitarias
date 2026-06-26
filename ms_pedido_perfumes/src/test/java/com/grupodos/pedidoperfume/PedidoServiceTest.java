package com.grupodos.pedidoperfume;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupodos.pedidoperfume.client.CatalogoClient;
import com.grupodos.pedidoperfume.client.UsuariosClient;
import com.grupodos.pedidoperfume.dto.PedidoRequestDTO;
import com.grupodos.pedidoperfume.dto.PedidoResponseDTO;
import com.grupodos.pedidoperfume.dto.PerfumeDTO;
import com.grupodos.pedidoperfume.dto.UsuarioDTO;
import com.grupodos.pedidoperfume.model.Pedido;
import com.grupodos.pedidoperfume.repository.PedidoRepository;
import com.grupodos.pedidoperfume.service.PedidoService;

@ExtendWith(MockitoExtension.class)
@DisplayName("PedidoService - Pruebas Unitarias")
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private CatalogoClient catalogoClient;

    @Mock
    private UsuariosClient usuariosClient;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void crear_perfumeYClienteValidos_retornaPedidoConTotal() {
        UsuarioDTO usuarioMock = new UsuarioDTO();
        usuarioMock.setNombre("rodrigo.kihn");
        when(usuariosClient.buscarPorNombre("rodrigo.kihn"))
            .thenReturn(List.of(usuarioMock));

        //Simular perfume válido
        PerfumeDTO perfumeMock = new PerfumeDTO();
        perfumeMock.setId(1L);
        perfumeMock.setNombre("Perfume Chanel");
        perfumeMock.setPrecio(new BigDecimal("100.00"));
        when(catalogoClient.obtenerPerfume(1L)).thenReturn(perfumeMock);

        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido p = invocation.getArgument(0);
            p.setId(1L); 
            return p;
        });

        //Ejecutar la lógica
        PedidoRequestDTO request = new PedidoRequestDTO();
        request.setCliente("rodrigo.kihn");
        request.setPerfumeId(1L);
        request.setCantidad(1);

        PedidoResponseDTO response = pedidoService.crear(request);

        assertNotNull(response);
        assertEquals("rodrigo.kihn", response.getCliente());
        assertEquals(1L, response.getId()); // Verificamos que el ID fue asignado
    }

    @Test
    @DisplayName("crear: lanza excepcion cuando el perfume no existe en catalogo (FeignException.NotFound)")
    void crear_perfumeNoExiste_lanzaExcepcion() {
        PedidoRequestDTO request = new PedidoRequestDTO();
        request.setCliente("rodrigo.kihn");
        request.setPerfumeId(99L);
        request.setCantidad(1);

        // Simulamos que el usuario existe para pasar la primera validación
        UsuarioDTO usuarioMock = new UsuarioDTO();
        usuarioMock.setNombre("rodrigo.kihn");
        when(usuariosClient.buscarPorNombre("rodrigo.kihn"))
            .thenReturn(List.of(usuarioMock));

        when(catalogoClient.obtenerPerfume(99L))
            .thenThrow(feign.FeignException.NotFound.class);

        assertThatThrownBy(() -> pedidoService.crear(request))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("no existe en el catalogo");

        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    @DisplayName("crear: lanza excepcion cuando ms_catalogo_perfume no esta disponible")
    void crear_catalogoNoDisponible_lanzaExcepcion() {
        PedidoRequestDTO request = new PedidoRequestDTO();
        request.setCliente("rodrigo.kihn");
        request.setPerfumeId(10L);
        request.setCantidad(1);

        // Simulamos que el usuario existe para pasar la primera validación
        UsuarioDTO usuarioMock = new UsuarioDTO();
        usuarioMock.setNombre("rodrigo.kihn");
        when(usuariosClient.buscarPorNombre("rodrigo.kihn"))
            .thenReturn(List.of(usuarioMock));

        when(catalogoClient.obtenerPerfume(10L))
            .thenThrow(feign.FeignException.class);

        assertThatThrownBy(() -> pedidoService.crear(request))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Verifique que ms_catalogo_perfume este corriendo");

        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    @DisplayName("crear: lanza excepcion cuando el cliente no existe en usuarios")
    void crear_clienteNoExiste_lanzaExcepcion() {
        PedidoRequestDTO request = new PedidoRequestDTO();
        request.setCliente("usuario.inexistente");
        request.setPerfumeId(10L);
        request.setCantidad(1);

        // Simulamos que el ms_usuarios lanza un 404 porque no encuentra al cliente
        when(usuariosClient.buscarPorNombre("usuario.inexistente"))
            .thenThrow(feign.FeignException.NotFound.class);

        assertThatThrownBy(() -> pedidoService.crear(request))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("no fue encontrado"); // Ajustado al mensaje en tu catch(FeignException.NotFound)

        verify(catalogoClient, never()).obtenerPerfume(anyLong());
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    @DisplayName("obtenerTodos: retorna todos los pedidos como DTO")
    void obtenerTodos_retornaListaCompleta() {
        //Creamos dos pedidos 
        Pedido p1 = new Pedido();
        p1.setId(1L);
        p1.setCliente("juan");
        p1.setTotal(new BigDecimal("100.00"));
        
        Pedido p2 = new Pedido();
        p2.setId(2L);
        p2.setCliente("maria");
        p2.setTotal(new BigDecimal("200.00"));
        when(pedidoRepository.findAll()).thenReturn(List.of(p1, p2));

        List<PedidoResponseDTO> resultado = pedidoService.obtenerTodos();

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(PedidoResponseDTO::getCliente)
                             .containsExactlyInAnyOrder("juan", "maria");
        
        // Verificamos que todos los totales sean mayores a cero
        assertThat(resultado).allMatch(p -> p.getTotal().compareTo(BigDecimal.ZERO) > 0);
        
        verify(pedidoRepository).findAll();
    }

    @Test
    @DisplayName("obtenerPorId: retorna el pedido cuando el id existe")
    void obtenerPorId_existente_retornaPedido() {
    Pedido pedido = new Pedido();
    pedido.setId(1L);
    pedido.setCliente("juan");

    when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
    Optional<PedidoResponseDTO> resultado = pedidoService.obtenerPorId(1L);

    assertThat(resultado).isPresent(); // Esto viene de AssertJ
    assertThat(resultado.get().getCliente()).isEqualTo("juan");
}

    @Test
    @DisplayName("obtenerPorCliente: retorna solo los pedidos del cliente indicado")
    void obtenerPorCliente_retornaSoloPedidosDelCliente() {
        // Simulamos dos pedidos del mismo cliente en la base de datos
        Pedido p1 = new Pedido();
        p1.setCliente("juan");
        p1.setNombrePerfume("Perfume Chanel");
        p1.setTotal(new BigDecimal("100.00"));

        Pedido p2 = new Pedido();
        p2.setCliente("juan");
        p2.setNombrePerfume("Perfume Dior");
        p2.setTotal(new BigDecimal("150.00"));

        when(pedidoRepository.findByCliente("juan")).thenReturn(List.of(p1, p2));

        List<PedidoResponseDTO> resultado = pedidoService.obtenerPorCliente("juan");

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(PedidoResponseDTO::getCliente)
                .allMatch(c -> c.equals("juan"));
        
        verify(pedidoRepository).findByCliente("juan");
    }
}
