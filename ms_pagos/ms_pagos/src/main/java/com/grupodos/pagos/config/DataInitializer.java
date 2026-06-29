package com.grupodos.pagos.config;

import com.grupodos.pagos.model.Pagos;
import com.grupodos.pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PagoRepository pagoRepository;
    @Override
    public void run(String... args) {
        if (pagoRepository.count() > 0) {
            log.info(">>> DataInitializer Pagos: datos ya existentes, se omite carga.");
            return;
        }

        pagoRepository.save(new Pagos(null, 101L, 45990.0, "Débito", "COMPLETADO", LocalDateTime.now().minusDays(2)));
        pagoRepository.save(new Pagos(null, 102L, 89990.0, "Crédito", "COMPLETADO", LocalDateTime.now().minusDays(1)));
        pagoRepository.save(new Pagos(null, 103L, 25500.0, "Transferencia", "PENDIENTE", LocalDateTime.now()));
        pagoRepository.save(new Pagos(null, 104L, 120000.0, "Crédito", "RECHAZADO", LocalDateTime.now()));
        log.info(">>> DataInitializer Pagos: {} registros de pago insertados de prueba.", pagoRepository.count());
    }
}