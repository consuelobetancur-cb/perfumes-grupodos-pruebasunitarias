package com.grupodos.carrito.service;

import com.grupodos.carrito.client.CatalogoClient;
import com.grupodos.carrito.dto.*;
import com.grupodos.carrito.model.Carrito;
import com.grupodos.carrito.model.ItemCarrito;
import com.grupodos.carrito.repository.CarritoRepository;
import com.grupodos.carrito.repository.ItemCarritoRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final CatalogoClient catalogoClient;

    @Transactional
    public CarritoResponseDTO crear(String usuario) {
        return mapToDTO(carritoRepository.save(new Carrito(usuario)));
    }

    public Optional<CarritoResponseDTO> obtenerPorId(Long id) {
        return carritoRepository.findById(id).map(this::mapToDTO);
    }

    public List<CarritoResponseDTO> obtenerPorUsuario(String usuario) {
        return carritoRepository.findByUsuario(usuario).stream().map(this::mapToDTO).toList();
    }

    @Transactional
    public CarritoResponseDTO agregarItem(Long carritoId, ItemCarritoRequestDTO dto) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado: " + carritoId));

        
        PerfumeDTO perfume = consultarPerfume(dto.getPerfumeId());

        
        Optional<ItemCarrito> existente = carrito.getItems().stream()
                .filter(i -> i.getPerfumeId().equals(dto.getPerfumeId()))
                .findFirst();

        if (existente.isPresent()) {
            existente.get().setCantidad(existente.get().getCantidad() + dto.getCantidad());
        } else { 
            carrito.getItems().add(new ItemCarrito(
                    perfume.getId(), perfume.getNombre(), perfume.getPrecio(),
                    dto.getCantidad(), carrito));
        }

        return mapToDTO(carritoRepository.save(carrito));
    }

    @Transactional
    public CarritoResponseDTO quitarItem(Long carritoId, Long itemId) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado: " + carritoId));
        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado: " + itemId));
        if (!item.getCarrito().getId().equals(carritoId))
            throw new RuntimeException("El item " + itemId + " no pertenece al carrito " + carritoId);
        
        carrito.getItems().remove(item);
        return mapToDTO(carritoRepository.save(carrito));
    }

    @Transactional
    public CarritoResponseDTO vaciar(Long carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado: " + carritoId));
        carrito.getItems().clear();
        return mapToDTO(carritoRepository.save(carrito));
    }

    @Transactional
    public void eliminar(Long carritoId) {
        carritoRepository.deleteById(carritoId);
    }

    
    private PerfumeDTO consultarPerfume(Long perfumeId) {
        try {
            return catalogoClient.obtenerPerfume(perfumeId);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("El perfume con id " + perfumeId + " no existe en el catálogo");
        } catch (FeignException e) {
            log.error("Error al contactar el microservicio de catálogo: {}", e.getMessage());
            throw new RuntimeException("No se pudo verificar el perfume. Verifique que el catálogo esté corriendo.");
        }
    }

    
    private CarritoResponseDTO mapToDTO(Carrito c) {
        CarritoResponseDTO dto = new CarritoResponseDTO();
        dto.setId(c.getId());
        dto.setUsuario(c.getUsuario());
        dto.setFechaCreacion(c.getFechaCreacion());

        List<ItemCarritoResponseDTO> items = c.getItems().stream().map(i -> {
            ItemCarritoResponseDTO iDto = new ItemCarritoResponseDTO();
            iDto.setId(i.getId());
            iDto.setPerfumeId(i.getPerfumeId());
            iDto.setNombrePerfume(i.getNombrePerfume()); 
            iDto.setCantidad(i.getCantidad());
            iDto.setPrecioUnitario(i.getPrecioUnitario());
            iDto.setSubtotal(i.getPrecioUnitario().multiply(BigDecimal.valueOf(i.getCantidad())));
            return iDto;
        }).toList();

        dto.setItems(items);
        
        
        dto.setTotal(items.stream()
                .map(ItemCarritoResponseDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return dto;
    }
}