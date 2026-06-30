package com.grupodos.favorito.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupodos.favorito.model.Favorito;


@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    List<Favorito> findByUsuario(String usuario);

    List<Favorito> findByPerfumeId(Long perfumeId);
    
    boolean existsByUsuarioAndPerfumeId(String usuario, Long perfumeId);
}
