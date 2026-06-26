package com.hvb.loyalty.utils;

import com.hvb.loyalty.dto.ClienteRequestDTO;
import com.hvb.loyalty.entity.Cliente;

public class ClienteUtil {

    public static void actualizarDatos(Cliente cliente, ClienteRequestDTO dto) {
        cliente.setNombre(dto.getNombre());
        cliente.setApellidos(dto.getApellidos());
        cliente.setTelefono(dto.getTelefono());
        cliente.setCorreo(dto.getCorreo());
    }
}