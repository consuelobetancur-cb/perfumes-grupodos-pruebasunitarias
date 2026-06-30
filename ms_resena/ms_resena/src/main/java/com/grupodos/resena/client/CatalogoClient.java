package com.grupodos.resena.client;

import com.grupodos.resena.dto.PerfumeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-catalogo", url = "${catalogo.service.url}")
public interface CatalogoClient {

    @GetMapping("/api/perfumes/{id}")
    PerfumeDTO obtenerPerfume(@PathVariable("id") Long id);
}