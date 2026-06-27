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
    private final TransaccionRepository transaccionRepository;  // ← NUEVO

    public DashboardService(ClienteRepository clienteRepository,
                            TarjetaRepository tarjetaRepository,
                            PremioRepository premioRepository,
                            CanjeRepository canjeRepository,
                            TransaccionRepository transaccionRepository) {  // ← NUEVO
        this.clienteRepository = clienteRepository;
        this.tarjetaRepository = tarjetaRepository;
        this.premioRepository = premioRepository;
        this.canjeRepository = canjeRepository;
        this.transaccionRepository = transaccionRepository;  // ← NUEVO
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

    public java.util.List<java.util.Map<String, Object>> puntosPorMes() {
        String[] meses = {"ENE","FEB","MAR","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
        int anioActual = java.time.LocalDate.now().getYear();

        // Inicializa los 12 meses en 0
        java.util.List<java.util.Map<String, Object>> resultado = new java.util.ArrayList<>();
        int[] puntosPorMes = new int[12];
        int[] canjesPorMes = new int[12];

        // Suma puntos de transacciones del año actual
        transaccionRepository.findAll().forEach(t -> {
            if (t.getFecha() != null && t.getFecha().getYear() == anioActual && t.getPuntos() != null) {
                int mes = t.getFecha().getMonthValue() - 1; // 0-11
                puntosPorMes[mes] += t.getPuntos();
            }
        });

        // Cuenta canjes del año actual
        canjeRepository.findAll().forEach(c -> {
            if (c.getFecha() != null && c.getFecha().getYear() == anioActual) {
                int mes = c.getFecha().getMonthValue() - 1;
                canjesPorMes[mes] += 1;
            }
        });

        for (int i = 0; i < 12; i++) {
            java.util.Map<String, Object> fila = new java.util.HashMap<>();
            fila.put("mes", meses[i]);
            fila.put("puntos", puntosPorMes[i]);
            fila.put("canjes", canjesPorMes[i]);
            resultado.add(fila);
        }
        return resultado;
    }
}