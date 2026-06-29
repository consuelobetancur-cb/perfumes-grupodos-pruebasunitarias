package com.grupodos.inventario.config;

import com.grupodos.inventario.model.Inventario;
import com.grupodos.inventario.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final InventarioRepository inventarioRepository;
    @Override
    public void run(String... args) {
        if (inventarioRepository.count() > 0) {
            log.info(">>> DataInitializer Inventario: datos ya existentes, se omite carga.");
            return;
        }
        inventarioRepository.save(new Inventario(null, 1L, "Santiago", 50, LocalDateTime.now()));
        inventarioRepository.save(new Inventario(null, 1L, "Viña del Mar", 15, LocalDateTime.now()));
        inventarioRepository.save(new Inventario(null, 2L, "Concepción", 30, LocalDateTime.now()));
        inventarioRepository.save(new Inventario(null, 3L, "Santiago", 0, LocalDateTime.now()));

        log.info(">>> DataInitializer Inventario: {} registros de stock insertados de prueba.", inventarioRepository.count());
    }
}