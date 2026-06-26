package com.grupodos.pedidoperfume.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupodos.pedidoperfume.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{
    List<Pedido> findByCliente(String cliente);
    
    List<Pedido> findByPerfumeId(Long perfumeId);
}

