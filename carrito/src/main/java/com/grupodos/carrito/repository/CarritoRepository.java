package com.grupodos.carrito.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupodos.carrito.model.Carrito;


@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    List<Carrito> findByUsuario(String Usuario);
}