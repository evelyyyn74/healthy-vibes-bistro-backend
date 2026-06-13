package com.hvb.loyalty.utils;

import com.hvb.loyalty.dto.UsuarioResponseDTO;
import com.hvb.loyalty.entity.Usuario;

public class UsuarioUtil {

    public static UsuarioResponseDTO toResponse(Usuario u) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setCorreo(u.getCorreo());
        dto.setTelefono(u.getTelefono());
        dto.setRol(u.getRol());
        dto.setActivo(u.getActivo());
        dto.setFechaCreacion(u.getFechaCreacion());
        return dto;
    }
}