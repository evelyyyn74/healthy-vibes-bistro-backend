package com.hvb.loyalty.service;

import com.hvb.loyalty.dto.CanjeRequestDTO;
import com.hvb.loyalty.dto.CanjeResponseDTO;
import com.hvb.loyalty.entity.*;
import com.hvb.loyalty.repository.CanjeRepository;
import com.hvb.loyalty.repository.PremioRepository;
import com.hvb.loyalty.repository.TarjetaRepository;
import com.hvb.loyalty.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CanjeService {

    private final CanjeRepository canjeRepository;
    private final TarjetaRepository tarjetaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PremioRepository premioRepository;

    public CanjeService(CanjeRepository canjeRepository,
                        TarjetaRepository tarjetaRepository,
                        UsuarioRepository usuarioRepository,
                        PremioRepository premioRepository) {
        this.canjeRepository = canjeRepository;
        this.tarjetaRepository = tarjetaRepository;
        this.usuarioRepository = usuarioRepository;
        this.premioRepository = premioRepository;
    }

    public List<CanjeResponseDTO> listar() {
        return canjeRepository.findAll().stream()
                .map(this::aResponse)
                .collect(Collectors.toList());
    }

    public CanjeResponseDTO obtenerPorId(Long id) {
        Canje canje = canjeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canje no encontrado"));
        return aResponse(canje);
    }

    public CanjeResponseDTO crear(CanjeRequestDTO dto) {
        Tarjeta tarjeta = tarjetaRepository.findById(dto.getTarjetaId())
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
        Premio premio = premioRepository.findById(dto.getPremioId())
                .orElseThrow(() -> new RuntimeException("Premio no encontrado"));

        int puntosDescontar = dto.getPuntosDescontados() != null
                ? dto.getPuntosDescontados()
                : (premio.getPuntosRequeridos() != null ? premio.getPuntosRequeridos() : 0);

        int acumulados = tarjeta.getPuntosAcumulados() != null ? tarjeta.getPuntosAcumulados() : 0;

        // Validar que tenga puntos suficientes
        if (acumulados < puntosDescontar) {
            throw new RuntimeException("Puntos insuficientes para canjear este premio");
        }

        Canje canje = new Canje();
        canje.setTarjeta(tarjeta);
        canje.setPremio(premio);
        canje.setPuntosDescontados(puntosDescontar);
        canje.setFecha(LocalDate.now());

        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            canje.setUsuario(usuario);
        }

        // Descontar puntos de la tarjeta
        tarjeta.setPuntosAcumulados(acumulados - puntosDescontar);
        int canjeados = tarjeta.getPuntosCanjeados() != null ? tarjeta.getPuntosCanjeados() : 0;
        tarjeta.setPuntosCanjeados(canjeados + puntosDescontar);
        tarjetaRepository.save(tarjeta);

        // Incrementar el contador de canjes del premio
        int realizados = premio.getCanjesRealizados() != null ? premio.getCanjesRealizados() : 0;
        premio.setCanjesRealizados(realizados + 1);
        premioRepository.save(premio);

        return aResponse(canjeRepository.save(canje));
    }

    public void eliminar(Long id) {
        Canje canje = canjeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canje no encontrado"));
        canjeRepository.delete(canje);
    }

    private CanjeResponseDTO aResponse(Canje c) {
        CanjeResponseDTO dto = new CanjeResponseDTO();
        dto.setId(c.getId());
        dto.setPuntosDescontados(c.getPuntosDescontados());
        dto.setFecha(c.getFecha());
        if (c.getTarjeta() != null) {
            dto.setTarjetaId(c.getTarjeta().getId());
            if (c.getTarjeta().getCliente() != null) {
                Cliente cliente = c.getTarjeta().getCliente();
                dto.setClienteNombre(cliente.getNombre() + " " + (cliente.getApellidos() != null ? cliente.getApellidos() : ""));
                dto.setClienteNivel(c.getTarjeta().getNivel() != null ? c.getTarjeta().getNivel().getNombre() : "—");
            }
        }
        if (c.getUsuario() != null) dto.setUsuarioNombre(c.getUsuario().getNombre());
        if (c.getPremio() != null) dto.setPremioNombre(c.getPremio().getNombre());
        return dto;
    }
}