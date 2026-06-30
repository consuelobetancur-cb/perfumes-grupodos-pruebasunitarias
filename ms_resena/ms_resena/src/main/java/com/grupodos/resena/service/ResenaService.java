package com.grupodos.resena.service;

import com.grupodos.resena.client.CatalogoClient;
import com.grupodos.resena.dto.PerfumeDTO;
import com.grupodos.resena.dto.ResenaPromedioDTO;
import com.grupodos.resena.dto.ResenaRequestDTO;
import com.grupodos.resena.dto.ResenaResponseDTO;
import com.grupodos.resena.model.Resena;
import com.grupodos.resena.repository.ResenaRepository;
import feign.FeignException;
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
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final CatalogoClient catalogoClient;

    @Transactional
    public ResenaResponseDTO crear(ResenaRequestDTO dto) {
        log.info("Creando reseña - perfumeId: {}, usuario: {}", dto.getPerfumeId(), dto.getUsuario());

        if (resenaRepository.existsByPerfumeIdAndUsuario(dto.getPerfumeId(), dto.getUsuario())) {
            throw new RuntimeException("El usuario " + dto.getUsuario() + " ya tiene una reseña para este perfume");
        }

        PerfumeDTO perfume = verificarPerfume(dto.getPerfumeId());

        Resena resena = new Resena();
        resena.setPerfumeId(dto.getPerfumeId());
        resena.setNombrePerfume(perfume.getNombre());
        resena.setUsuario(dto.getUsuario());
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());
        resena.setFechaResena(LocalDateTime.now());

        Resena guardada = resenaRepository.save(resena);
        log.info("Reseña creada (id: {}) para perfume '{}'", guardada.getId(), perfume.getNombre());
        
        return mapToDTO(guardada);
    }

    public List<ResenaResponseDTO> obtenerTodas() {
        return resenaRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public Optional<ResenaResponseDTO> obtenerPorId(Long id) {
        return resenaRepository.findById(id).map(this::mapToDTO);
    }

    public List<ResenaResponseDTO> obtenerPorPerfume(Long perfumeId) {
        return resenaRepository.findByPerfumeIdOrderByFechaResenaDesc(perfumeId)
                .stream().map(this::mapToDTO).toList();
    }

    public List<ResenaResponseDTO> obtenerPorUsuario(String usuario) {
        return resenaRepository.findByUsuario(usuario)
                .stream().map(this::mapToDTO).toList();
    }

    public ResenaPromedioDTO obtenerPromedio(Long perfumeId) {
        verificarPerfume(perfumeId);

        Double promedio = resenaRepository.promedioCalificacion(perfumeId);
        long total = resenaRepository.findByPerfumeId(perfumeId).size();

        double promedioFinal = promedio != null 
                ? Math.round(promedio * 10.0) / 10.0 
                : 0.0;

        return new ResenaPromedioDTO(perfumeId, promedioFinal, total);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!resenaRepository.existsById(id)) {
            throw new RuntimeException("Reseña no encontrada con id " + id);
        }
        resenaRepository.deleteById(id);
        log.info("Reseña id {} eliminada", id);
    }

    private PerfumeDTO verificarPerfume(Long perfumeId) {
        try {
            return catalogoClient.obtenerPerfume(perfumeId);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("El perfume con id " + perfumeId + " no existe en el catálogo");
        } catch (FeignException e) {
            log.error("Error al contactar ms-catalogo: {}", e.getMessage());
            throw new RuntimeException("No se pudo verificar el perfume. Verifique que ms-catalogo esté corriendo.");
        }
    }

    private ResenaResponseDTO mapToDTO(Resena r) {
        ResenaResponseDTO dto = new ResenaResponseDTO();
        dto.setId(r.getId());
        dto.setPerfumeId(r.getPerfumeId());
        dto.setNombrePerfume(r.getNombrePerfume());
        dto.setUsuario(r.getUsuario());
        dto.setCalificacion(r.getCalificacion());
        dto.setComentario(r.getComentario());
        dto.setFechaResena(r.getFechaResena());
        return dto;
    }
}