package com.grupodos.pagos;

import com.grupodos.pagos.TestDataFactory;
import com.grupodos.pagos.dto.PagoRequestDTO;
import com.grupodos.pagos.dto.PagoResponseDTO;
import com.grupodos.pagos.model.Pagos;
import com.grupodos.pagos.repository.PagoRepository;
import com.grupodos.pagos.service.PagoService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PagoService - Pruebas Unitarias")
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    @Test
    @DisplayName("crear: guarda pago correctamente si no existe uno completado para el pedido")
    void crear_pagoValido_guardaExitosamente() {
        PagoRequestDTO request = TestDataFactory.unPagoRequest();
        when(pagoRepository.existsByPedidoIdAndEstado(request.getPedidoId(), "COMPLETADO")).thenReturn(false);
        
        Pagos pagoGuardado = TestDataFactory.unPago();
        when(pagoRepository.save(any(Pagos.class))).thenReturn(pagoGuardado);

        PagoResponseDTO resultado = pagoService.crear(request);

        assertThat(resultado.getPedidoId()).isEqualTo(pagoGuardado.getPedidoId());
        verify(pagoRepository).save(any(Pagos.class));
    }

    @Test
    @DisplayName("crear: lanza error si el pedido ya tiene pago completado")
    void crear_pagoDuplicado_lanzaExcepcion() {
        PagoRequestDTO request = TestDataFactory.unPagoRequest();
        when(pagoRepository.existsByPedidoIdAndEstado(request.getPedidoId(), "COMPLETADO")).thenReturn(true);

        assertThatThrownBy(() -> pagoService.crear(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ya tiene un pago completado");
    }

    @Test
    @DisplayName("actualizarEstado: cambia el estado correctamente")
    void actualizarEstado_pagoExistente_actualizaEstado() {
        Pagos pago = TestDataFactory.unPago();
        when(pagoRepository.findById(pago.getId())).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pagos.class))).thenReturn(pago);

        PagoResponseDTO resultado = pagoService.actualizarEstado(pago.getId(), "APROBADO");

        assertThat(resultado.getEstado()).isEqualTo("APROBADO");
        verify(pagoRepository).save(any(Pagos.class));
    }
@Test
    @DisplayName("obtenerTodos: retorna la lista completa de pagos")
    void obtenerTodos_retornaLista() {
        Pagos pago = TestDataFactory.unPago();
        when(pagoRepository.findAll()).thenReturn(java.util.List.of(pago));

        java.util.List<PagoResponseDTO> resultado = pagoService.obtenerTodos();

        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getId()).isEqualTo(pago.getId());
        verify(pagoRepository).findAll();
    }

    @Test
    @DisplayName("obtenerPorId: retorna el pago si existe")
    void obtenerPorId_pagoExiste_retornaPago() {
        Pagos pago = TestDataFactory.unPago();
        when(pagoRepository.findById(pago.getId())).thenReturn(Optional.of(pago));

        Optional<PagoResponseDTO> resultado = pagoService.obtenerPorId(pago.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(pago.getId());
        verify(pagoRepository).findById(pago.getId());
    }

    @Test
    @DisplayName("buscarPorPedido: retorna los pagos asociados a un pedido")
    void buscarPorPedido_retornaLista() {
        Pagos pago = TestDataFactory.unPago();
        when(pagoRepository.findByPedidoId(pago.getPedidoId())).thenReturn(java.util.List.of(pago));

        java.util.List<PagoResponseDTO> resultado = pagoService.buscarPorPedido(pago.getPedidoId());

        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getPedidoId()).isEqualTo(pago.getPedidoId());
        verify(pagoRepository).findByPedidoId(pago.getPedidoId());
    }

    @Test
    @DisplayName("actualizarEstado: lanza error si el pago no existe")
    void actualizarEstado_pagoNoExiste_lanzaExcepcion() {
        when(pagoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pagoService.actualizarEstado(999L, "APROBADO"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no encontrado");
        
        verify(pagoRepository).findById(999L);
        verify(pagoRepository, never()).save(any(Pagos.class));
    }
}