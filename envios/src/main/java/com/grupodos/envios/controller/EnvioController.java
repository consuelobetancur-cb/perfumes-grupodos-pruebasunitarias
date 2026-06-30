package com.grupodos.envios.controller;

import com.grupodos.envios.model.Envio;
import com.grupodos.envios.service.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @PostMapping
    public ResponseEntity<Envio> crearEnvio(@RequestBody Envio envio) {
        Envio nuevoEnvio = envioService.registrarEnvio(envio);
        return new ResponseEntity<>(nuevoEnvio, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> obtenerEnvio(@PathVariable Long id) {
        return ResponseEntity.ok(envioService.obtenerPorId(id));
    }
}