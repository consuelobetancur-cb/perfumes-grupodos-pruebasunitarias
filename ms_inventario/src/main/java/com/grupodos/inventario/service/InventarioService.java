package com.grupodos.inventario.service;

import com.grupodos.inventario.dto.InventarioRequestDTO;
import com.grupodos.inventario.dto.InventarioResponseDTO;
import com.grupodos.inventario.model.Inventario;
import com.grupodos.inventario.repository.InventarioRepository;
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
public class InventarioService {
    private final InventarioRepository inventarioRepository;

    public List<InventarioResponseDTO> obtenerTodos() {
        return inventarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }
    public Optional<InventarioResponseDTO> obtenerPorId(Long id) {
        return inventarioRepository.findById(id).map(this::toResponse);
    }
    public List<InventarioResponseDTO> buscarPorPerfume(Long perfumeId) {
        return inventarioRepository.findByPerfumeId(perfumeId).stream()
                .map(this::toResponse)
                .toList();
    }
    public List<InventarioResponseDTO> buscarPorSucursal(String sucursal) {
        return inventarioRepository.findBySucursal(sucursal).stream()
                .map(this::toResponse)
                .toList();
    }
    @Transactional
    public InventarioResponseDTO guardar(InventarioRequestDTO dto) {
        Optional<Inventario> existente = inventarioRepository.findByPerfumeIdAndSucursal(dto.getPerfumeId(), dto.getSucursal());
        
        Inventario inventario;
        if (existente.isPresent()) {
            inventario = existente.get();
            inventario.setCantidad(dto.getCantidad());
            log.info("Actualizando stock existente para perfume id {} en sucursal {}", dto.getPerfumeId(), dto.getSucursal());
        } else {
            inventario = new Inventario();
            inventario.setPerfumeId(dto.getPerfumeId());
            inventario.setSucursal(dto.getSucursal());
            inventario.setCantidad(dto.getCantidad());
            log.info("Creando nuevo registro de stock para perfume id {} en sucursal {}", dto.getPerfumeId(), dto.getSucursal());
        }
        inventario.setFechaActualizacion(LocalDateTime.now());
        Inventario guardado = inventarioRepository.save(inventario);
        return toResponse(guardado);
    }
    private InventarioResponseDTO toResponse(Inventario i) {
        InventarioResponseDTO dto = new InventarioResponseDTO();
        dto.setId(i.getId());
        dto.setPerfumeId(i.getPerfumeId());
        dto.setSucursal(i.getSucursal());
        dto.setCantidad(i.getCantidad());
        dto.setFechaActualizacion(i.getFechaActualizacion());
        return dto;
    }
}