package com.grupodos.favorito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}
