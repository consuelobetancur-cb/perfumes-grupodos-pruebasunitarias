package com.grupodos.perfumes.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.grupodos.perfumes.model.Categoria;
import com.grupodos.perfumes.model.Perfume;
import com.grupodos.perfumes.repository.CategoriaRepository;
import com.grupodos.perfumes.repository.PerfumeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final PerfumeRepository perfumeRepository;

    @Override
    public void run(String... args) {
        if (categoriaRepository.count() > 0) {
            log.info(">>> DataInitializer: datos ya existentes, se omite carga.");
            return;
        }

    Categoria hombres = categoriaRepository.save(new Categoria(null, "Masculino", "Fragancias masculinas"));
    Categoria mujeres = categoriaRepository.save(new Categoria(null, "Femenino", "Fragancias femeninas"));

    perfumeRepository.save(new Perfume(null,"Sauvage", "Dior", new BigDecimal("120.00"), 15, hombres));
    perfumeRepository.save(new Perfume(null, "Acqua di Gio", "Giorgio Armani", new BigDecimal("95.50"), 20, hombres));
    perfumeRepository.save(new Perfume(null, "La Vie Est Belle", "Lancôme", new BigDecimal("110.00"), 10, mujeres));
    perfumeRepository.save(new Perfume(null, "J'adore", "Dior", new BigDecimal("130.00"), 5, mujeres));

    log.info(">>> DataInitializer: {} categorías y {} perfumes insertados.",
            categoriaRepository.count(), perfumeRepository.count());

    }
}
