package com.grupodos.perfumes.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.grupodos.perfumes.model.Perfume;

public interface PerfumeRepository extends JpaRepository<Perfume,Long> {

    List<Perfume> findByNombreContainingIgnoreCase(String nombre);

    List<Perfume> findByMarcaContainingIgnoreCase(String marca);

    List<Perfume> findByPrecioBetween(BigDecimal min, BigDecimal max);

    List<Perfume> findByStockLessThan(Integer stock);

    boolean existsByCategoria_Id(Long categoriaId);

    boolean existsByCategoriaId(Long categoriaId);
    
    @Query("SELECT p FROM Perfume p WHERE p.categoria.id = :categoriaId")
    List<Perfume> findByCategoriaId(@Param("categoriaId") Long categoriaId);

    @Query("SELECT p FROM Perfume p WHERE p.precio <= :precioMax ORDER BY p.precio DESC")
    List<Perfume> findPrecioBajoPresupuesto(@Param("precioMax") BigDecimal precioMax);

    @Query("SELECT p FROM Perfume p WHERE p.stock < :limite ORDER BY p.stock ASC")
    List<Perfume> buscarStockCritico(@Param("limite") Integer limite);

    @Query(value = "SELECT * FROM perfumes WHERE nombre LIKE CONCAT('%', :texto, '%') OR marca LIKE CONCAT('%', :texto, '%')", nativeQuery = true)
    List<Perfume> buscarGlobalNativo(@Param("texto") String texto);

    
}