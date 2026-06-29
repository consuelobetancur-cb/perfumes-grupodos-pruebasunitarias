package com.grupodos.promociones;

import com.grupodos.promociones.dto.AplicarDescuentoDTO;
import com.grupodos.promociones.dto.PromocionRequestDTO;
import com.grupodos.promociones.model.Promocion;
import com.grupodos.promociones.repository.PromocionRepository;
import com.grupodos.promociones.service.PromocionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PromocionService - Pruebas Unitarias con DataFaker")
public class PromocionServiceTest {

    @Mock
    private PromocionRepository promocionRepository;

    @InjectMocks
    private PromocionService promocionService;

    @Test
    @DisplayName("crear: Debe normalizar el código en mayúsculas y guardar la promoción activa")
    void crear_DatosValidos_GuardaPromocionCorrectamente() {
        // Arrange
        PromocionRequestDTO dto = TestDataFactory.unaPromocionRequestVálida();
        dto.setCodigo("  promo-test-10  "); // Con espacios y minúsculas para probar normalización
        
        Promocion promoMockGuardada = new Promocion();
        promoMockGuardada.setId(1L);
        promoMockGuardada.setCodigo("PROMO-TEST-10");
        promoMockGuardada.setPorcentajeDescuento(dto.getPorcentajeDescuento());
        promoMockGuardada.setFechaExpiracion(dto.getFechaExpiracion());
        promoMockGuardada.setActivo(true);

        when(promocionRepository.save(any(Promocion.class))).thenReturn(promoMockGuardada);

        // Act
        Promocion resultado = promocionService.crear(dto);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getCodigo()).isEqualTo("PROMO-TEST-10");
        assertThat(resultado.getActivo()).isTrue();
        verify(promocionRepository, times(1)).save(any(Promocion.class));
    }

    @Test
    @DisplayName("obtenerTodas: Debe retornar la lista completa de promociones")
    void obtenerTodas_ExistenPromociones_RetornaLista() {
        // Arrange
        Promocion promo = TestDataFactory.unaPromocionActivaYVigente("DESC20", 20.0);
        when(promocionRepository.findAll()).thenReturn(List.of(promo));

        // Act
        List<Promocion> resultado = promocionService.obtenerTodas();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCodigo()).isEqualTo("DESC20"); // Método .get(0) nativo de Java arreglado
        verify(promocionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("calcularDescuento: Debe aplicar el descuento correctamente si el cupón está vigente")
    void calcularDescuento_CuponVigente_AplicaDescuento() {
        // Arrange
        String codigoCupon = "PERFUME10";
        Double totalCarrito = 50000.0;
        Promocion promo = TestDataFactory.unaPromocionActivaYVigente(codigoCupon, 10.0);

        when(promocionRepository.findByCodigoAndActivoTrue("PERFUME10")).thenReturn(Optional.of(promo));

        // Act
        AplicarDescuentoDTO resultado = promocionService.calcularDescuento("  perfume10  ", totalCarrito);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getMontoDescuento()).isEqualTo(5000.0);
        assertThat(resultado.getTotalConDescuento()).isEqualTo(45000.0);
        assertThat(resultado.getMensaje()).isEqualTo("Cupón aplicado con éxito.");
    }

    @Test
    @DisplayName("calcularDescuento: Debe denegar el descuento si el cupón expiró")
    void calcularDescuento_CuponExpirado_RetornaMensajeDeExpiracion() {
        // Arrange
        String codigoCupon = "VINTAGE50";
        Double totalCarrito = 30000.0;
        Promocion promoExpirada = TestDataFactory.unaPromocionExpirada(codigoCupon);

        when(promocionRepository.findByCodigoAndActivoTrue("VINTAGE50")).thenReturn(Optional.of(promoExpirada));

        // Act
        AplicarDescuentoDTO resultado = promocionService.calcularDescuento(codigoCupon, totalCarrito);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getMontoDescuento()).isEqualTo(0.0);
        assertThat(resultado.getTotalConDescuento()).isEqualTo(totalCarrito);
        assertThat(resultado.getMensaje()).isEqualTo("El cupón ha expirado.");
    }

    @Test
    @DisplayName("calcularDescuento: Debe retornar mensaje de inexistencia si el cupón no se encuentra")
    void calcularDescuento_CuponInexistente_RetornaMensajeDeError() {
        // Arrange
        String codigoInvalido = "NOEXISTO";
        when(promocionRepository.findByCodigoAndActivoTrue(codigoInvalido)).thenReturn(Optional.empty());

        // Act
        AplicarDescuentoDTO resultado = promocionService.calcularDescuento(codigoInvalido, 10000.0);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getMontoDescuento()).isEqualTo(0.0);
        assertThat(resultado.getMensaje()).isEqualTo("Cupón inválido o inexistente.");
    }
}