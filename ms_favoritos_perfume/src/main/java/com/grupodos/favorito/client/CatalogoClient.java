package com.grupodos.favorito.client;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.grupodos.favorito.dto.PerfumeDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogoClient {
        private final WebClient catalogoWebClient;

        public Optional<PerfumeDTO> buscarPerfume(Long perfumeId) {
    try {
        PerfumeDTO perfume = catalogoWebClient.get()
                .uri("/api/perfumes/{id}", perfumeId)
                .retrieve()
                .bodyToMono(PerfumeDTO.class)
                .block();
        return Optional.ofNullable(perfume);
} catch (WebClientResponseException.NotFound e) {
        return Optional.empty();
    } catch (Exception e) {
        log.error("Error al conectar con el ms_catalogo: {}", e.getMessage());
        throw new RuntimeException("El servicio de catálogo de perfumes no está disponible en este momento");
    }
}
}
        
