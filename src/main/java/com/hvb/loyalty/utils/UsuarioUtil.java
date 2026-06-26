package com.hvb.loyalty.utils;

import com.hvb.loyalty.dto.UsuarioRequestDTO;
import com.hvb.loyalty.entity.Usuario;

public class UsuarioUtil {

    public static void actualizarDatos(Usuario usuario, UsuarioRequestDTO dto) {
        usuario.setNombre(dto.getNombre());
        usuario.setUsuario(dto.getUsuario());
        usuario.setRol(dto.getRol());
        usuario.setCorreo(dto.getCorreo());
        usuario.setTelefono(dto.getTelefono());
    }
}