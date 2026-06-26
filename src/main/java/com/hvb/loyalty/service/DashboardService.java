package com.hvb.loyalty.service;

import com.hvb.loyalty.dto.DashboardResponseDTO;
import com.hvb.loyalty.repository.*;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final ClienteRepository clienteRepository;
    private final TarjetaRepository tarjetaRepository;
    private final PremioRepository premioRepository;
    private final CanjeRepository canjeRepository;

    public DashboardService(ClienteRepository clienteRepository,
                            TarjetaRepository tarjetaRepository,
                            PremioRepository premioRepository,
                            CanjeRepository canjeRepository) {
        this.clienteRepository = clienteRepository;
        this.tarjetaRepository = tarjetaRepository;
        this.premioRepository = premioRepository;
        this.canjeRepository = canjeRepository;
    }

    public DashboardResponseDTO obtenerResumen() {
        DashboardResponseDTO dto = new DashboardResponseDTO();

        dto.setTotalClientes(clienteRepository.count());
        dto.setClientesActivos(
                clienteRepository.findAll().stream()
                        .filter(c -> Boolean.TRUE.equals(c.getActivo()))
                        .count()
        );
        dto.setTotalTarjetas(tarjetaRepository.count());
        dto.setTotalPremios(premioRepository.count());
        dto.setTotalCanjes(canjeRepository.count());

        return dto;
    }
}