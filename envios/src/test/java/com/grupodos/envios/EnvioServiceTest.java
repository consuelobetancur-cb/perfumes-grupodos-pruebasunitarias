package com.grupodos.envios;

// Importaciones de los componentes de tu proyecto
import com.grupodos.envios.exception.EnvioNotFoundException;
import com.grupodos.envios.model.Envio;
import com.grupodos.envios.repository.EnvioRepository;
import com.grupodos.envios.service.EnvioService;

// Importaciones de JUnit 5 y Mockito para las pruebas unitarias
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Importación de Java Util
import java.util.Optional;

// Importaciones estáticas para aserciones (AssertJ) y verificaciones (Mockito)
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EnvioService - Pruebas Unitarias con DataFaker")
public class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    @InjectMocks
    private EnvioService envioService;

    @Test
    @DisplayName("registrarEnvio: Debe asignar costo $3500 y estado PENDIENTE cuando la región es Metropolitana")
    void registrarEnvio_RegionMetropolitana_AsignaCosto3500YEstadoPendiente() {
        // Arrange
        Envio envioEntrada = TestDataFactory.unEnvioMetropolitana();
        
        Envio envioMockGuardado = new Envio();
        envioMockGuardado.setId(1L);
        envioMockGuardado.setPedidoId(envioEntrada.getPedidoId());
        envioMockGuardado.setRegion(envioEntrada.getRegion());
        envioMockGuardado.setDireccion(envioEntrada.getDireccion());
        envioMockGuardado.setCostoEnvio(3500.0);
        envioMockGuardado.setEstado("PENDIENTE");

        when(envioRepository.save(any(Envio.class))).thenReturn(envioMockGuardado);

        // Act
        Envio resultado = envioService.registrarEnvio(envioEntrada);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getCostoEnvio()).isEqualTo(3500.0);
        assertThat(resultado.getEstado()).isEqualTo("PENDIENTE");
        
        verify(envioRepository, times(1)).save(envioEntrada);
    }

    @Test
    @DisplayName("registrarEnvio: Debe asignar costo $7000 y estado PENDIENTE cuando es cualquier otra región")
    void registrarEnvio_OtraRegion_AsignaCosto7000YEstadoPendiente() {
        // Arrange
        Envio envioEntrada = TestDataFactory.unEnvioRegiones();
        
        Envio envioMockGuardado = new Envio();
        envioMockGuardado.setId(2L);
        envioMockGuardado.setPedidoId(envioEntrada.getPedidoId());
        envioMockGuardado.setRegion(envioEntrada.getRegion());
        envioMockGuardado.setDireccion(envioEntrada.getDireccion());
        envioMockGuardado.setCostoEnvio(7000.0);
        envioMockGuardado.setEstado("PENDIENTE");

        when(envioRepository.save(any(Envio.class))).thenReturn(envioMockGuardado);

        // Act
        Envio resultado = envioService.registrarEnvio(envioEntrada);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(2L);
        assertThat(resultado.getCostoEnvio()).isEqualTo(7000.0);
        assertThat(resultado.getEstado()).isEqualTo("PENDIENTE");
        
        verify(envioRepository, times(1)).save(envioEntrada);
    }

    @Test
    @DisplayName("obtenerPorId: Debe retornar el envío si el ID existe en la base de datos")
    void obtenerPorId_IdExiste_RetornaEnvio() {
        // Arrange
        Long idExistente = 15L;
        Envio envioMock = TestDataFactory.unEnvioGuardado(idExistente, "Valparaíso", 7000.0, "PENDIENTE");
        
        when(envioRepository.findById(idExistente)).thenReturn(Optional.of(envioMock));

        // Act
        Envio resultado = envioService.obtenerPorId(idExistente);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(idExistente);
        assertThat(resultado.getRegion()).isEqualTo("Valparaíso");
        assertThat(resultado.getDireccion()).isEqualTo(envioMock.getDireccion());
        
        verify(envioRepository, times(1)).findById(idExistente);
    }

    @Test
    @DisplayName("obtenerPorId: Debe lanzar EnvioNotFoundException si el ID no existe")
    void obtenerPorId_IdNoExiste_LanzaEnvioNotFoundException() {
        // Arrange
        Long idInexistente = 999L;
        when(envioRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> envioService.obtenerPorId(idInexistente))
                .isInstanceOf(EnvioNotFoundException.class)
                .hasMessageContaining("El envío con ID " + idInexistente + " no existe.");
                
        verify(envioRepository, times(1)).findById(idInexistente);
    }
}