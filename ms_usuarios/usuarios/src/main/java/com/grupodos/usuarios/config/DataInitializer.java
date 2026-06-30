package com.grupodos.usuarios.config;

import com.grupodos.usuarios.model.Usuario;
import com.grupodos.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UsuarioRepository usuarioRepository;
    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info(">>> DataInitializer: datos ya existentes, se omite carga.");
            return;
        }
        usuarioRepository.save(new Usuario(null, "Ana Valdés", "ana@perfulandia.cl", "555-0001", "Av. Providencia 123, Santiago", true));
        usuarioRepository.save(new Usuario(null, "Carlos Gómez", "carlos@perfulandia.cl", "555-0002", "Calle Marina 456, Viña del Mar", true));
        usuarioRepository.save(new Usuario(null, "María López", "maria@perfulandia.cl", "555-0003", "Caupolicán 789, Concepción", true));
        usuarioRepository.save(new Usuario(null, "Héctor Monsalves", "hector@gmail.com", "555-0004", "Barrio Meiggs 101, Santiago", true));
        log.info(">>> DataInitializer: {} usuarios insertados de prueba.", usuarioRepository.count());
    }
}