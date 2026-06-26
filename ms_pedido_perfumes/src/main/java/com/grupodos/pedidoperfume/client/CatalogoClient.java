package com.grupodos.pedidoperfume.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.grupodos.pedidoperfume.dto.PerfumeDTO;

@FeignClient(name = "ms-catalogo-perfume", url = "${catalogo.service.url}")
public interface CatalogoClient {

    @GetMapping("/api/perfumes/{id}")
    PerfumeDTO obtenerPerfume(@PathVariable("id") Long id);
}
