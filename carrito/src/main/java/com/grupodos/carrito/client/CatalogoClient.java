package com.grupodos.carrito.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.grupodos.carrito.dto.PerfumeDTO;

@FeignClient(name = "microservicio-catalogo", url = "${catalogo.service.url}")
public interface CatalogoClient {

    @GetMapping("/api/perfumes/{id}") 
    PerfumeDTO obtenerPerfume(@PathVariable Long id);
} 


