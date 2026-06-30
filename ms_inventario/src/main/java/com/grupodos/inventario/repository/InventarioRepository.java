package com.grupodos.inventario.repository;

import com.grupodos.inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    List<Inventario> findByPerfumeId(Long perfumeId);
    List<Inventario> findBySucursal(String sucursal);
    Optional<Inventario> findByPerfumeIdAndSucursal(Long perfumeId, String sucursal);
}