package com.hvb.loyalty.service;

import com.hvb.loyalty.dto.TarjetaResponseDTO;
import com.hvb.loyalty.entity.Cliente;
import com.hvb.loyalty.entity.Nivel;
import com.hvb.loyalty.entity.Tarjeta;
import com.hvb.loyalty.repository.NivelRepository;
import com.hvb.loyalty.repository.TarjetaRepository;
import org.springframework.stereotype.Service;
import com.hvb.loyalty.repository.CanjeRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TarjetaService {

    private final TarjetaRepository tarjetaRepository;
    private final NivelRepository nivelRepository;
    private final CanjeRepository canjeRepository;

    public TarjetaService(TarjetaRepository tarjetaRepository, NivelRepository nivelRepository,  CanjeRepository canjeRepository) {
        this.tarjetaRepository = tarjetaRepository;
        this.nivelRepository = nivelRepository;
        this.canjeRepository = canjeRepository;
    }


    public List<TarjetaResponseDTO> listar() {
        return tarjetaRepository.findAll().stream()
                .map(this::aResponse)
                .collect(Collectors.toList());
    }

    public TarjetaResponseDTO obtenerPorId(Long id) {
        Tarjeta tarjeta = tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
        return aResponse(tarjeta);
    }

    public TarjetaResponseDTO obtenerPorCodigoQr(String codigoQr) {
        Tarjeta tarjeta = tarjetaRepository.findByCodigoQr(codigoQr)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
        return aResponse(tarjeta);
    }

    /**
     * Crea una tarjeta automáticamente para un cliente nuevo.
     * Se llama desde ClienteService al registrar un cliente.
     */
    public Tarjeta crearParaCliente(Cliente cliente) {
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setCliente(cliente);
        tarjeta.setCodigoQr("HVB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        tarjeta.setSerial("SN-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        tarjeta.setPuntosAcumulados(0);
        tarjeta.setPuntosCanjeados(0);
        tarjeta.setActivo(true);

        // Asignar nivel inicial: el de orden más bajo, o el primero que exista
        Nivel nivelInicial = nivelRepository.findAll().stream()
                .filter(n -> n.getOrden() != null)
                .min(java.util.Comparator.comparingInt(Nivel::getOrden))
                .orElse(nivelRepository.findAll().stream().findFirst()
                        .orElseThrow(() -> new RuntimeException(
                                "No hay niveles configurados. Crea al menos un nivel antes de registrar clientes.")));
        tarjeta.setNivel(nivelInicial);

        return tarjetaRepository.save(tarjeta);
    }

    public void cambiarEstado(Long id, Boolean activo) {
        Tarjeta tarjeta = tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
        tarjeta.setActivo(activo);
        tarjetaRepository.save(tarjeta);
    }

    private TarjetaResponseDTO aResponse(Tarjeta t) {
        TarjetaResponseDTO dto = new TarjetaResponseDTO();
        dto.setId(t.getId());
        dto.setCodigoQr(t.getCodigoQr());
        dto.setPuntosAcumulados(t.getPuntosAcumulados());
        dto.setPuntosCanjeados(t.getPuntosCanjeados());
        dto.setActivo(t.getActivo());

        if (t.getCliente() != null) {
            dto.setClienteId(t.getCliente().getId());
            dto.setClienteNombre(t.getCliente().getNombre());
            dto.setClienteApellidos(t.getCliente().getApellidos());
            dto.setClienteTelefono(t.getCliente().getTelefono());
            dto.setClienteCorreo(t.getCliente().getCorreo());
        }

        if (t.getNivel() != null) {
            dto.setNivelNombre(t.getNivel().getNombre());
            dto.setNivelPuntosMax(t.getNivel().getPuntosMax());

            // Calcular puntos para recompensa
            int puntosActuales = t.getPuntosAcumulados() != null ? t.getPuntosAcumulados() : 0;
            int puntosMax = t.getNivel().getPuntosMax() != null ? t.getNivel().getPuntosMax() : 0;
            dto.setPuntosParaRecompensa(Math.max(puntosMax - puntosActuales, 0));
            dto.setRecompensasObtenidas(canjeRepository.countByTarjetaId(t.getId()));
        }

        return dto;
    }


    public void reiniciarPuntos(Long clienteId) {
        Tarjeta tarjeta = tarjetaRepository.findAll().stream()
                .filter(t -> t.getCliente() != null && t.getCliente().getId().equals(clienteId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Este cliente no tiene tarjeta"));
        tarjeta.setPuntosAcumulados(0);
        tarjetaRepository.save(tarjeta);
    }
}