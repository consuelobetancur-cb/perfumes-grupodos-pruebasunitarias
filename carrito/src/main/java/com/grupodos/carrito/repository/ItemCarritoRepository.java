package com.grupodos.carrito.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupodos.carrito.model.ItemCarrito;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {

    List<ItemCarrito> findByCarritoId(Long carritoId);
}
