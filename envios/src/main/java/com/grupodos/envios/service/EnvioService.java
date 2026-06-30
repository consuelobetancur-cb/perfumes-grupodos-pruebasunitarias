package com.grupodos.envios.service;

import com.grupodos.envios.exception.EnvioNotFoundException;
import com.grupodos.envios.model.Envio;
import com.grupodos.envios.repository.EnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

    public Envio registrarEnvio(Envio envio) {
        if (envio.getRegion().equalsIgnoreCase("Metropolitana")) {
            envio.setCostoEnvio(3500.0);
        } else {
            envio.setCostoEnvio(7000.0);
        }
        envio.setEstado("PENDIENTE");
        return envioRepository.save(envio);
    }

    public Envio obtenerPorId(Long id) {
    return envioRepository.findById(id)
            .orElseThrow(() -> new EnvioNotFoundException("El envío con ID " + id + " no existe."));
    }
}