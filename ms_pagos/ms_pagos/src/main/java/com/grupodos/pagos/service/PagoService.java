package com.grupodos.pagos.service;

import com.grupodos.pagos.dto.PagoRequestDTO;
import com.grupodos.pagos.dto.PagoResponseDTO;
import com.grupodos.pagos.model.Pagos;
import com.grupodos.pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagoService {
    private final PagoRepository pagoRepository;

    public List<PagoResponseDTO> obtenerTodos() {
        return pagoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }
    public Optional<PagoResponseDTO> obtenerPorId(Long id) {
        return pagoRepository.findById(id).map(this::toResponse);
    }
    public List<PagoResponseDTO> buscarPorPedido(Long pedidoId) {
        return pagoRepository.findByPedidoId(pedidoId).stream()
                .map(this::toResponse)
                .toList();
    }
    @Transactional
    public PagoResponseDTO crear(PagoRequestDTO dto) {
        if (pagoRepository.existsByPedidoIdAndEstado(dto.getPedidoId(), "COMPLETADO")) {
            throw new RuntimeException("El pedido " + dto.getPedidoId() + " ya tiene un pago completado.");
        }
        Pagos pago = new Pagos();
        pago.setPedidoId(dto.getPedidoId());
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setEstado("PENDIENTE");
        pago.setFechaPago(LocalDateTime.now());
        Pagos guardado = pagoRepository.save(pago);
        log.info("Pago registrado: id {} para el pedido {}", guardado.getId(), guardado.getPedidoId());
        return toResponse(guardado);
    }
    @Transactional
    public PagoResponseDTO actualizarEstado(Long id, String nuevoEstado) {
        Pagos pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago con id " + id + " no encontrado"));
        pago.setEstado(nuevoEstado);
        Pagos guardado = pagoRepository.save(pago);
        log.info("Estado del pago id {} actualizado a {}", id, nuevoEstado);
        return toResponse(guardado);
    }
    private PagoResponseDTO toResponse(Pagos p) {
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setId(p.getId());
        dto.setPedidoId(p.getPedidoId());
        dto.setMonto(p.getMonto());
        dto.setMetodoPago(p.getMetodoPago());
        dto.setEstado(p.getEstado());
        dto.setFechaPago(p.getFechaPago());
        return dto;
    }
}