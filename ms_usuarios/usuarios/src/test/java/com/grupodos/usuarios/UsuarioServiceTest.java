package com.grupodos.usuarios;

import com.grupodos.usuarios.dto.UsuarioRequestDTO;
import com.grupodos.usuarios.dto.UsuarioResponseDTO;
import com.grupodos.usuarios.model.Usuario;
import com.grupodos.usuarios.repository.UsuarioRepository;
import com.grupodos.usuarios.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService - Pruebas Unitarias")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("obtenerTodos: retorna todos los usuarios como DTO")
    void obtenerTodos_retornaListaCompleta() {
        Usuario u1 = TestDataFactory.unUsuario();
        Usuario u2 = TestDataFactory.unUsuario();
        when(usuarioRepository.findAll()).thenReturn(List.of(u1, u2));

        List<UsuarioResponseDTO> resultado = usuarioService.obtenerTodos();

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(UsuarioResponseDTO::getNombre)
                .containsExactly(u1.getNombre(), u2.getNombre());
        verify(usuarioRepository).findAll();
    }

    @Test
    @DisplayName("obtenerPorId: retorna el usuario cuando existe")
    void obtenerPorId_usuarioExiste_retornaDTO() {
        Usuario usuario = TestDataFactory.unUsuario();
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));

        Optional<UsuarioResponseDTO> resultado = usuarioService.obtenerPorId(usuario.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo(usuario.getNombre());
        verify(usuarioRepository).findById(usuario.getId());
    }

    @Test
    @DisplayName("buscarPorNombre: retorna una lista de usuarios cuando el nombre existe")
    void buscarPorNombre_encontrado_retornaListaDTO() {
        Usuario usuario = TestDataFactory.unUsuario();
        when(usuarioRepository.findByNombre(usuario.getNombre())).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> resultado = usuarioService.buscarPorNombre(usuario.getNombre());

        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getNombre()).isEqualTo(usuario.getNombre());
        verify(usuarioRepository).findByNombre(usuario.getNombre());
    }

    @Test
    @DisplayName("buscarPorEmail: retorna el usuario cuando existe")
    void buscarPorEmail_encontrado_retornaDTO() {
        Usuario usuario = TestDataFactory.unUsuario();
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        Optional<UsuarioResponseDTO> resultado = usuarioService.buscarPorEmail(usuario.getEmail());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEmail()).isEqualTo(usuario.getEmail());
        verify(usuarioRepository).findByEmail(usuario.getEmail());
    }

    @Test
    @DisplayName("crear: guarda exitosamente cuando el email no está repetido")
    void crear_emailDisponible_guardaYRetornaDTO() {
        UsuarioRequestDTO request = TestDataFactory.unUsuarioRequest();

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setId(1L);
        usuarioGuardado.setNombre(request.getNombre());
        usuarioGuardado.setEmail(request.getEmail());
        usuarioGuardado.setTelefono(request.getTelefono());
        usuarioGuardado.setDireccionEnvio(request.getDireccionEnvio());
        usuarioGuardado.setActivo(true);

        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        UsuarioResponseDTO resultado = usuarioService.crear(request);

        assertThat(resultado.getNombre()).isEqualTo(request.getNombre());
        verify(usuarioRepository).existsByEmail(request.getEmail());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("crear: lanza excepcion cuando el email ya existe")
    void crear_emailDuplicado_lanzaExcepcion() {
        UsuarioRequestDTO request = TestDataFactory.unUsuarioRequest();
        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.crear(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ya existe un usuario con el email");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("desactivar: realiza borrado logico bajando el estado activo a false")
    void desactivar_usuarioExistente_cambiaEstadoAFalse() {
        Usuario usuario = TestDataFactory.unUsuario();
        usuario.setActivo(true);

        Usuario usuarioDesactivado = new Usuario();
        usuarioDesactivado.setId(usuario.getId());
        usuarioDesactivado.setNombre(usuario.getNombre());
        usuarioDesactivado.setEmail(usuario.getEmail());
        usuarioDesactivado.setActivo(false);

        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioDesactivado);

        UsuarioResponseDTO resultado = usuarioService.desactivar(usuario.getId());

        assertThat(resultado.getActivo()).isFalse();
        verify(usuarioRepository).findById(usuario.getId());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("eliminar: busca el usuario y llama a delete")
    void eliminar_usuarioExistente_llamaDelete() {
        Usuario usuario = TestDataFactory.unUsuario();
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));

        usuarioService.eliminar(usuario.getId());

        verify(usuarioRepository).findById(usuario.getId());
        verify(usuarioRepository).delete(usuario);
    }
}