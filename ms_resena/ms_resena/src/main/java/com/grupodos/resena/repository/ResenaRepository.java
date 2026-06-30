package com.grupodos.resena.repository;

import com.grupodos.resena.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Long> {

    List<Resena> findByPerfumeId(Long perfumeId);

    List<Resena> findByUsuario(String usuario);

    List<Resena> findByPerfumeIdOrderByFechaResenaDesc(Long perfumeId);

    boolean existsByPerfumeIdAndUsuario(Long perfumeId, String usuario);

    @Query("SELECT AVG(r.calificacion) FROM Resena r WHERE r.perfumeId = :perfumeId")
    Double promedioCalificacion(@Param("perfumeId") Long perfumeId);
}