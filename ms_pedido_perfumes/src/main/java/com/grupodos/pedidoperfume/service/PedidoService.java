package com.grupodos.pedidoperfume.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;


import com.grupodos.pedidoperfume.client.CatalogoClient;
import com.grupodos.pedidoperfume.client.UsuariosClient;

import com.grupodos.pedidoperfume.dto.PedidoRequestDTO;
import com.grupodos.pedidoperfume.dto.PedidoResponseDTO;
import com.grupodos.pedidoperfume.dto.PerfumeDTO;
import com.grupodos.pedidoperfume.dto.UsuarioDTO;
import com.grupodos.pedidoperfume.model.Pedido;
import com.grupodos.pedidoperfume.repository.PedidoRepository;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final CatalogoClient catalogoClient;
    private final UsuariosClient usuariosClient;
    @Transactional
    public PedidoResponseDTO crear(PedidoRequestDTO dto) {
        validarCliente(dto.getCliente());
        PerfumeDTO perfume = consultarPerfume(dto.getPerfumeId());

        Pedido pedido = new Pedido();
        pedido.setCliente(dto.getCliente());
        pedido.setPerfumeId(perfume.getId());
        pedido.setNombrePerfume(perfume.getNombre());
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstadoPedido("pago pendiente");
        pedido.setCantidad(dto.getCantidad());

        pedido.setOpcionEnvio(dto.getOpcionEnvio());
        pedido.setCuponDescuento(dto.getCuponDescuento());
        
        BigDecimal precioUnitario = perfume.getPrecio(); 
        BigDecimal cantidadConstruida = BigDecimal.valueOf(dto.getCantidad());
        pedido.setTotal(precioUnitario.multiply(cantidadConstruida));

        Pedido guardado = pedidoRepository.save(pedido);
        log.info("Pedido creado id={} para el perfume '{}', total a pagar: ${}", 
                 guardado.getId(), perfume.getNombre(), guardado.getTotal());
                return mapToDTO(guardado);
    }

     public List<PedidoResponseDTO> obtenerTodos() {
        return pedidoRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public Optional<PedidoResponseDTO> obtenerPorId(Long id) {
        return pedidoRepository.findById(id).map(this::mapToDTO);
    }

    public List<PedidoResponseDTO> obtenerPorCliente(String cliente) {
        return pedidoRepository.findByCliente(cliente).stream().map(this::mapToDTO).toList();
    }


    @Transactional
    public void eliminar(Long id) {
        pedidoRepository.deleteById(id);
    }

   private void validarCliente(String cliente) {
        try {
            
            List<UsuarioDTO> usuarios = usuariosClient.buscarPorNombre(cliente);
            
            if (usuarios == null || usuarios.isEmpty()) {
                throw new RuntimeException("El cliente '" + cliente + "' no existe en el sistema");
            }
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("El cliente '" + cliente + "' no fue encontrado.");
        } catch (FeignException e) {
            log.error("Error al contactar ms_usuarios: {}", e.getMessage());
            throw new RuntimeException("No se pudo verificar el cliente. Verifique que ms_usuarios esté corriendo en el puerto 8084.");
        }
    }

    private PerfumeDTO consultarPerfume(Long perfumeId) {
        try {
            return catalogoClient.obtenerPerfume(perfumeId);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("El perfume con id " + perfumeId + " no existe en el catalogo");
        } catch (FeignException e) {
            log.error("Error al contactar ms_catalogo_perfume: {}", e.getMessage());
            throw new RuntimeException("No se pudo verificar el perfume. Verifique que ms_catalogo_perfume este corriendo.");
        }
    }

    private PedidoResponseDTO mapToDTO(Pedido p) {
    PedidoResponseDTO dto = new PedidoResponseDTO();
    dto.setId(p.getId());
    dto.setCliente(p.getCliente());
    dto.setFechaPedido(p.getFechaPedido());
    dto.setCantidad(p.getCantidad());
    dto.setPerfumeId(p.getPerfumeId());
    dto.setNombrePerfume(p.getNombrePerfume());
    dto.setEstadoPedido(p.getEstadoPedido());
    dto.setTotal(p.getTotal());
    dto.setOpcionEnvio(p.getOpcionEnvio());
    dto.setCuponDescuento(p.getCuponDescuento());
    return dto;
    }

}
