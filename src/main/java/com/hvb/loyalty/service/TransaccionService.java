package com.hvb.loyalty.service;

import com.hvb.loyalty.dto.TransaccionRequestDTO;
import com.hvb.loyalty.dto.TransaccionResponseDTO;
import com.hvb.loyalty.entity.Tarjeta;
import com.hvb.loyalty.entity.Transaccion;
import com.hvb.loyalty.entity.Usuario;
import com.hvb.loyalty.repository.TarjetaRepository;
import com.hvb.loyalty.repository.TransaccionRepository;
import com.hvb.loyalty.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final TarjetaRepository tarjetaRepository;
    private final UsuarioRepository usuarioRepository;

    public TransaccionService(TransaccionRepository transaccionRepository,
                              TarjetaRepository tarjetaRepository,
                              UsuarioRepository usuarioRepository) {
        this.transaccionRepository = transaccionRepository;
        this.tarjetaRepository = tarjetaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<TransaccionResponseDTO> listar() {
        return transaccionRepository.findAll().stream()
                .map(this::aResponse)
                .collect(Collectors.toList());
    }

    public TransaccionResponseDTO obtenerPorId(Long id) {
        Transaccion t = transaccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
        return aResponse(t);
    }

    public TransaccionResponseDTO crear(TransaccionRequestDTO dto) {
        Transaccion t = new Transaccion();
        t.setTipo(dto.getTipo());
        t.setPuntos(dto.getPuntos());
        t.setDescripcion(dto.getDescripcion());
        t.setCategoria(dto.getCategoria());
        t.setFecha(LocalDate.now());

        // Asociar tarjeta
        Tarjeta tarjeta = tarjetaRepository.findById(dto.getTarjetaId())
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
        t.setTarjeta(tarjeta);

        // Asociar usuario que la registró
        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            t.setUsuario(usuario);
        }

        // Actualizar puntos de la tarjeta según el tipo
        int puntos = dto.getPuntos() != null ? dto.getPuntos() : 0;
        int acumulados = tarjeta.getPuntosAcumulados() != null ? tarjeta.getPuntosAcumulados() : 0;

        if ("CANJE".equalsIgnoreCase(dto.getTipo())) {
            tarjeta.setPuntosAcumulados(acumulados - puntos); // resta
        } else {
            tarjeta.setPuntosAcumulados(acumulados + puntos); // VISITA o COMPRA suman
        }
        tarjetaRepository.save(tarjeta);

        return aResponse(transaccionRepository.save(t));
    }

    public void eliminar(Long id) {
        Transaccion t = transaccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
        transaccionRepository.delete(t);
    }

    private TransaccionResponseDTO aResponse(Transaccion t) {
        TransaccionResponseDTO dto = new TransaccionResponseDTO();
        dto.setId(t.getId());
        dto.setTipo(t.getTipo());
        dto.setPuntos(t.getPuntos());
        dto.setDescripcion(t.getDescripcion());
        dto.setCategoria(t.getCategoria());
        dto.setFecha(t.getFecha());
        if (t.getTarjeta() != null) dto.setTarjetaId(t.getTarjeta().getId());
        if (t.getUsuario() != null) dto.setUsuarioNombre(t.getUsuario().getNombre());
        return dto;
    }
}