package com.grupodos.favorito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupodos.favorito.client.CatalogoClient;
import com.grupodos.favorito.repository.FavoritoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("FavoritoService - Pruebas Unitarias")
class FavoritoServiceTest {

    @Mock
    private FavoritoRepository favoritorepository;

    @Mock
    private CatalogoClient catalogoClient;

}
