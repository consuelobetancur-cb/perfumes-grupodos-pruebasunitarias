package com.grupodos.pedidoperfume.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.grupodos.pedidoperfume.dto.UsuarioDTO;
import java.util.List;

@FeignClient(name = "ms-usuarios", url = "${usuarios.service.url}")
public interface UsuariosClient {
    @GetMapping("/api/usuarios/nombre/{nombre}")
    List<UsuarioDTO> buscarPorNombre(@PathVariable String nombre);
}
