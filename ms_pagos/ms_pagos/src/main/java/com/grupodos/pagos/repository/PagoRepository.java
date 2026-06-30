package com.grupodos.pagos.repository;

import com.grupodos.pagos.model.Pagos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pagos, Long> {
    List<Pagos> findByPedidoId(Long pedidoId);

    boolean existsByPedidoIdAndEstado(Long pedidoId, String estado);
}