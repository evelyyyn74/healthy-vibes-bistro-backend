package com.hvb.loyalty.service;

import com.hvb.loyalty.dto.NivelRequestDTO;
import com.hvb.loyalty.dto.NivelResponseDTO;
import com.hvb.loyalty.entity.Nivel;
import com.hvb.loyalty.repository.NivelRepository;
import com.hvb.loyalty.utils.NivelUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NivelService {

    private final NivelRepository nivelRepository;

    public NivelService(NivelRepository nivelRepository) {
        this.nivelRepository = nivelRepository;
    }

    public List<NivelResponseDTO> listar() {
        return nivelRepository.findAll().stream()
                .map(this::aResponse)
                .collect(Collectors.toList());
    }

    public NivelResponseDTO obtenerPorId(Long id) {
        Nivel nivel = nivelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));
        return aResponse(nivel);
    }

    public NivelResponseDTO crear(NivelRequestDTO dto) {
        Nivel nivel = new Nivel();
        NivelUtil.actualizarDatos(nivel, dto);
        return aResponse(nivelRepository.save(nivel));
    }

    public NivelResponseDTO actualizar(Long id, NivelRequestDTO dto) {
        Nivel nivel = nivelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));
        NivelUtil.actualizarDatos(nivel, dto);
        return aResponse(nivelRepository.save(nivel));
    }

    public void eliminar(Long id) {
        Nivel nivel = nivelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));
        nivelRepository.delete(nivel);
    }

    private NivelResponseDTO aResponse(Nivel n) {
        NivelResponseDTO dto = new NivelResponseDTO();
        dto.setId(n.getId());
        dto.setNombre(n.getNombre());
        dto.setPuntosMin(n.getPuntosMin());
        dto.setPuntosMax(n.getPuntosMax());
        dto.setOrden(n.getOrden());
        dto.setPuntosPorVisita(n.getPuntosPorVisita());
        return dto;
    }
}