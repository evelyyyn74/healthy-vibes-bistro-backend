package com.hvb.loyalty.service;

import com.hvb.loyalty.dto.NivelResponseDTO;
import com.hvb.loyalty.dto.PremioRequestDTO;
import com.hvb.loyalty.dto.PremioResponseDTO;
import com.hvb.loyalty.entity.Nivel;
import com.hvb.loyalty.entity.Premio;
import com.hvb.loyalty.repository.NivelRepository;
import com.hvb.loyalty.repository.PremioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PremioService {

    private final PremioRepository premioRepository;
    private final NivelRepository nivelRepository;

    public PremioService(PremioRepository premioRepository, NivelRepository nivelRepository) {
        this.premioRepository = premioRepository;
        this.nivelRepository = nivelRepository;
    }

    public List<PremioResponseDTO> listar() {
        return premioRepository.findAll().stream()
                .map(this::aResponse)
                .collect(Collectors.toList());
    }

    public PremioResponseDTO obtenerPorId(Long id) {
        Premio premio = premioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Premio no encontrado"));
        return aResponse(premio);
    }

    public PremioResponseDTO crear(PremioRequestDTO dto) {
        Premio premio = new Premio();
        aplicarDatos(premio, dto);
        premio.setCanjesRealizados(0); // arranca en 0
        return aResponse(premioRepository.save(premio));
    }

    public PremioResponseDTO actualizar(Long id, PremioRequestDTO dto) {
        Premio premio = premioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Premio no encontrado"));
        aplicarDatos(premio, dto);
        return aResponse(premioRepository.save(premio));
    }

    public void eliminar(Long id) {
        Premio premio = premioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Premio no encontrado"));
        premioRepository.delete(premio);
    }

    private void aplicarDatos(Premio premio, PremioRequestDTO dto) {
        premio.setNombre(dto.getNombre());
        premio.setDescripcion(dto.getDescripcion());
        premio.setPuntosRequeridos(dto.getPuntosRequeridos());
        premio.setLimiteCanjes(dto.getLimiteCanjes());
        premio.setFechaInicio(dto.getFechaInicio());
        premio.setFechaFin(dto.getFechaFin());
        premio.setDisponible(dto.getDisponible());
        premio.setAplicaPara(dto.getAplicaPara());

        if (dto.getNivelId() != null) {
            Nivel nivel = nivelRepository.findById(dto.getNivelId())
                    .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));
            premio.setNivel(nivel);
        } else {
            premio.setNivel(null);
        }
    }

    private PremioResponseDTO aResponse(Premio p) {
        PremioResponseDTO dto = new PremioResponseDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setPuntosRequeridos(p.getPuntosRequeridos());
        dto.setLimiteCanjes(p.getLimiteCanjes());
        dto.setFechaInicio(p.getFechaInicio());
        dto.setFechaFin(p.getFechaFin());
        dto.setDisponible(p.getDisponible());
        dto.setAplicaPara(p.getAplicaPara());
        dto.setCanjesRealizados(p.getCanjesRealizados());

        if (p.getNivel() != null) {
            Nivel n = p.getNivel();
            NivelResponseDTO nivelDto = new NivelResponseDTO();
            nivelDto.setId(n.getId());
            nivelDto.setNombre(n.getNombre());
            nivelDto.setPuntosMin(n.getPuntosMin());
            nivelDto.setPuntosMax(n.getPuntosMax());
            nivelDto.setOrden(n.getOrden());
            nivelDto.setPuntosPorVisita(n.getPuntosPorVisita());
            dto.setNivel(nivelDto);
        }
        return dto;
    }
}