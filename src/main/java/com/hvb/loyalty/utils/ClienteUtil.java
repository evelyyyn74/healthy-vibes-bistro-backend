package com.hvb.loyalty.utils;

import com.hvb.loyalty.dto.ClienteResponseDTO;
import com.hvb.loyalty.entity.Cliente;

public class ClienteUtil {

    public static ClienteResponseDTO toResponse(Cliente c) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setCorreo(c.getCorreo());
        dto.setTelefono(c.getTelefono());
        dto.setNivel(c.getNivel());
        dto.setPuntos(c.getPuntos());
        dto.setActivo(c.getActivo());
        dto.setFechaRegistro(c.getFechaRegistro());
        dto.setFechaUltimoReinicio(c.getFechaUltimoReinicio());
        if (c.getTarjeta() != null) {
            dto.setCodigoQr(c.getTarjeta().getCodigoQr());
        }
        return dto;
    }
}