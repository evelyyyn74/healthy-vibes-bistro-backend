package com.hvb.loyalty.utils;

import com.hvb.loyalty.dto.NivelRequestDTO;
import com.hvb.loyalty.entity.Nivel;

public class NivelUtil {

    public static void actualizarDatos(Nivel nivel, NivelRequestDTO dto) {
        nivel.setNombre(dto.getNombre());
        nivel.setPuntosMin(dto.getPuntosMin());
        nivel.setPuntosMax(dto.getPuntosMax());
        nivel.setOrden(dto.getOrden());
        nivel.setPuntosPorVisita(dto.getPuntosPorVisita());
    }
}