package com.grupodos.promociones.repository;

import com.grupodos.promociones.model.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PromocionRepository extends JpaRepository<Promocion, Long> {
    Optional<Promocion> findByCodigoAndActivoTrue(String codigo);
}