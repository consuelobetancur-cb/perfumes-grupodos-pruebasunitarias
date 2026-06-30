package com.grupodos.promociones.service;

import com.grupodos.promociones.dto.AplicarDescuentoDTO;
import com.grupodos.promociones.dto.PromocionRequestDTO;
import com.grupodos.promociones.model.Promocion;
import com.grupodos.promociones.repository.PromocionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromocionService {

    private final PromocionRepository promocionRepository;

    public Promocion crear(PromocionRequestDTO dto) {
        Promocion promo = new Promocion();
        promo.setCodigo(dto.getCodigo().toUpperCase().trim());
        promo.setPorcentajeDescuento(dto.getPorcentajeDescuento());
        promo.setFechaExpiracion(dto.getFechaExpiracion());
        promo.setActivo(true);
        return promocionRepository.save(promo);
    }

    public List<Promocion> obtenerTodas() {
        return promocionRepository.findAll();
    }

    public AplicarDescuentoDTO calcularDescuento(String codigo, Double totalCarrito) {
        return promocionRepository.findByCodigoAndActivoTrue(codigo.toUpperCase().trim())
                .map(promo -> {
                    if (promo.getFechaExpiracion().isBefore(LocalDate.now())) {
                        return new AplicarDescuentoDTO(0.0, totalCarrito, "El cupón ha expirado.");
                    }
                    
                    Double montoDescuento = totalCarrito * (promo.getPorcentajeDescuento() / 100.0);
                    Double totalConDescuento = totalCarrito - montoDescuento;
                    
                    return new AplicarDescuentoDTO(montoDescuento, totalConDescuento, "Cupón aplicado con éxito.");
                })
                .orElse(new AplicarDescuentoDTO(0.0, totalCarrito, "Cupón inválido o inexistente."));
    }
}