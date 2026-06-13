package com.hvb.loyalty.service;

import com.hvb.loyalty.dto.ClienteRequestDTO;
import com.hvb.loyalty.dto.ClienteResponseDTO;
import com.hvb.loyalty.entity.Cliente;
import com.hvb.loyalty.entity.Tarjeta;
import com.hvb.loyalty.repository.ClienteRepository;
import com.hvb.loyalty.utils.ClienteUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteResponseDTO> listar() {
        return clienteRepository.findAll().stream().map(ClienteUtil::toResponse).toList();
    }

    public ClienteResponseDTO obtener(Long id) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return ClienteUtil.toResponse(c);
    }

    public ClienteResponseDTO crear(ClienteRequestDTO dto) {
        if (clienteRepository.existsByCorreo(dto.getCorreo()))
            throw new RuntimeException("Ya existe un cliente con ese correo");

        Cliente c = new Cliente();
        c.setNombre(dto.getNombre());
        c.setCorreo(dto.getCorreo());
        c.setTelefono(dto.getTelefono());

        // Se crea su tarjeta digital automáticamente
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setCliente(c);
        c.setTarjeta(tarjeta);

        return ClienteUtil.toResponse(clienteRepository.save(c));
    }

    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (!c.getCorreo().equals(dto.getCorreo()) && clienteRepository.existsByCorreo(dto.getCorreo()))
            throw new RuntimeException("Ya existe un cliente con ese correo");

        c.setNombre(dto.getNombre());
        c.setCorreo(dto.getCorreo());
        c.setTelefono(dto.getTelefono());
        return ClienteUtil.toResponse(clienteRepository.save(c));
    }

    public ClienteResponseDTO reiniciarPuntos(Long id) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        c.setPuntos(0);
        c.setFechaUltimoReinicio(LocalDateTime.now());
        return ClienteUtil.toResponse(clienteRepository.save(c));
    }

    public ClienteResponseDTO cambiarEstado(Long id) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        c.setActivo(!c.getActivo());
        return ClienteUtil.toResponse(clienteRepository.save(c));
    }
}