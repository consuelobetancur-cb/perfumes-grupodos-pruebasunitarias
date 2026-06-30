package com.grupodos.favorito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupodos.favorito.client.CatalogoClient;
import com.grupodos.favorito.client.UsuariosClient;
import com.grupodos.favorito.dto.FavoritoRequestDTO;
import com.grupodos.favorito.dto.FavoritoResponseDTO;
import com.grupodos.favorito.dto.PerfumeDTO;
import com.grupodos.favorito.dto.UsuarioDTO;
import com.grupodos.favorito.model.Favorito;
import com.grupodos.favorito.repository.FavoritoRepository;
import com.grupodos.favorito.service.favoritoService;

@ExtendWith(MockitoExtension.class)
@DisplayName("FavoritoService - Pruebas Unitarias")
class FavoritoServiceTest {

    @Mock
    private FavoritoRepository favoritorepository;

    @Mock
    private CatalogoClient catalogoClient;

    @Mock
    private UsuariosClient usuariosClient;

    @InjectMocks
    private favoritoService Favoritoservice;

   @Test
    @DisplayName("agregar: guarda el favorito cuando usuario y perfume existen")
    void agregar_usuarioYPerfumeValidos_guardaFavorito() {
        PerfumeDTO perfume = TestDataFactory.unPerfumeDTO();
        UsuarioDTO usuario = TestDataFactory.unUsuarioDTO();
        
        FavoritoRequestDTO request = TestDataFactory.unFavoritoRequest(
                perfume.getId(), usuario.getNombre());

        Favorito favoritoGuardado = TestDataFactory.unFavorito(
                perfume.getId(), perfume.getNombre(), usuario.getNombre());

        when(usuariosClient.buscarPorNombre(usuario.getNombre()))
                .thenReturn(Optional.of(usuario));
        
        when(catalogoClient.buscarPerfume(perfume.getId()))
                .thenReturn(Optional.of(perfume));
        
        when(favoritorepository.existsByUsuarioAndPerfumeId(usuario.getNombre(), perfume.getId()))
                .thenReturn(false);
        
        when(favoritorepository.save(any(Favorito.class)))
                .thenReturn(favoritoGuardado);

        FavoritoResponseDTO resultado = Favoritoservice.agregar(request);

        assertThat(resultado.getNombre()).isEqualTo(perfume.getNombre());
        assertThat(resultado.getUsuario()).isEqualTo(usuario.getNombre());
        assertThat(resultado.getPerfumeId()).isEqualTo(perfume.getId());
        
        verify(favoritorepository).save(any(Favorito.class));
    }
    
    @Test
    @DisplayName("agregar: lanza excepcion cuando el usuario no existe")
    void agregar_usuarioNoExiste_lanzaExcepcion() {

        PerfumeDTO perfume = TestDataFactory.unPerfumeDTO();
        FavoritoRequestDTO request = TestDataFactory.unFavoritoRequest(
                perfume.getId(), "usuarioInexistente");

        when(usuariosClient.buscarPorNombre("usuarioInexistente"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> Favoritoservice.agregar(request))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("no existe"); 

        verify(catalogoClient, never()).buscarPerfume(anyLong());
        verify(favoritorepository, never()).save(any(Favorito.class));
    }

    @Test
    @DisplayName("agregar: lanza excepcion cuando el perfume no existe en catalogo")
    void agregar_perfumeNoExiste_lanzaExcepcion() {
        //given
        UsuarioDTO usuario = TestDataFactory.unUsuarioDTO();
        FavoritoRequestDTO request = TestDataFactory.unFavoritoRequest(99L, usuario.getNombre());

        //When
        when(usuariosClient.buscarPorNombre(usuario.getNombre()))
                .thenReturn(Optional.of(usuario));
        
        // Simulamos que el catálogo no encuentra el perfume solicitado
        when(catalogoClient.buscarPerfume(99L)).thenReturn(Optional.empty());

        // Then
       assertThatThrownBy(() -> Favoritoservice.agregar(request))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("no existe en el catálogo");

        verify(favoritorepository, never()).save(any(Favorito.class));
    }

    @Test
    @DisplayName("agregar: lanza excepcion cuando el usuario ya tiene ese perfume en favoritos")
    void agregar_favoritoDuplicado_lanzaExcepcion() {
        PerfumeDTO perfume = TestDataFactory.unPerfumeDTO();
        UsuarioDTO usuario = TestDataFactory.unUsuarioDTO();
        FavoritoRequestDTO request = TestDataFactory.unFavoritoRequest(
                perfume.getId(), usuario.getNombre());

        when(usuariosClient.buscarPorNombre(usuario.getNombre()))
                .thenReturn(Optional.of(usuario));
        when(catalogoClient.buscarPerfume(perfume.getId()))
                .thenReturn(Optional.of(perfume));
        
        when(favoritorepository.existsByUsuarioAndPerfumeId(usuario.getNombre(), perfume.getId()))
        .thenReturn(true);
        
        assertThatThrownBy(() -> Favoritoservice.agregar(request))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("ya tiene este perfume en favoritos"); 

        verify(favoritorepository, never()).save(any());
    }

    @Test
    @DisplayName("listarTodos: retorna todos los favoritos como DTO")
    void listarTodos_retornaListaCompleta() {

        PerfumeDTO p1 = TestDataFactory.unPerfumeDTO();
        PerfumeDTO p2 = TestDataFactory.unPerfumeDTO(); 
        p2.setNombre("Otro Perfume");
        p2.setId(2L);
        
        Favorito f1 = TestDataFactory.unFavorito(p1.getId(), p1.getNombre(), "juan");
        Favorito f2 = TestDataFactory.unFavorito(p2.getId(), p2.getNombre(), "maria");
        
        when(favoritorepository.findAll()).thenReturn(List.of(f1, f2));

        List<FavoritoResponseDTO> resultado = Favoritoservice.listarTodos();

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(FavoritoResponseDTO::getUsuario)
                .containsExactly("juan", "maria");

        verify(favoritorepository).findAll();
    }

    @Test
    @DisplayName("listarPorUsuario: retorna solo los favoritos del usuario indicado")
    void listarPorUsuario_retornaFavoritosDelUsuario() {

        PerfumeDTO p1 = TestDataFactory.unPerfumeDTO();

        PerfumeDTO p2 = new PerfumeDTO();
        p2.setId(2L);
        p2.setNombre("Bleu de Chanel");

        PerfumeDTO p3 = new PerfumeDTO();
        p3.setId(3L);
        p3.setNombre("Dior Sauvage");

        Favorito f1 = TestDataFactory.unFavorito(p1.getId(), p1.getNombre(), "juan");
        Favorito f2 = TestDataFactory.unFavorito(p2.getId(), p2.getNombre(), "juan");
        Favorito f3 = TestDataFactory.unFavorito(p3.getId(), p3.getNombre(), "juan");

        when(favoritorepository.findByUsuario("juan")).thenReturn(List.of(f1, f2, f3));

        List<FavoritoResponseDTO> resultado = Favoritoservice.listarPorUsuario("juan");

        assertThat(resultado).hasSize(3);
        assertThat(resultado).allMatch(f -> f.getUsuario().equals("juan"));
   
        verify(favoritorepository).findByUsuario("juan");
    }

    @Test
    @DisplayName("eliminar: elimina el favorito cuando el id existe")
    void eliminar_favoritoExistente_llamaDelete() {
        PerfumeDTO p1 = TestDataFactory.unPerfumeDTO();
        Favorito favorito = TestDataFactory.unFavorito(p1.getId(), p1.getNombre(), "juan");
        
        when(favoritorepository.findById(favorito.getId()))
                .thenReturn(Optional.of(favorito));

        Favoritoservice.eliminar(favorito.getId());

        verify(favoritorepository).findById(favorito.getId());
        verify(favoritorepository).delete(favorito);
    }

     @Test
     @DisplayName("eliminar: lanza excepcion cuando el id no existe")
     void eliminar_favoritoNoExiste_lanzaExcepcion() {
    
    when(favoritorepository.findById(999L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> Favoritoservice.eliminar(999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("no encontrado");

    verify(favoritorepository, never()).delete(any());
}
}
