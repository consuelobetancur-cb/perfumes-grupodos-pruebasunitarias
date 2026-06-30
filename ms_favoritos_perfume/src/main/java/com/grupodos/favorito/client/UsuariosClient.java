package com.grupodos.favorito.client;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.grupodos.favorito.dto.UsuarioDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsuariosClient {
    private final WebClient usuariosWebClient;

    public Optional<UsuarioDTO> buscarPorNombre(String nombre) {
        try {
           
            List<UsuarioDTO> usuarios = usuariosWebClient.get()
                    .uri("/api/usuarios/nombre/{nombre}", nombre)
                    .retrieve()
                    .bodyToFlux(UsuarioDTO.class) 
                    .collectList()                
                    .block();                     

            
            if (usuarios != null && !usuarios.isEmpty()) {
                return Optional.of(usuarios.get(0));
            }
            
            return Optional.empty();

        } catch (WebClientResponseException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error al conectar con ms_usuarios: {}", e.getMessage(), e);
            return Optional.empty(); 
        }
    }
}
