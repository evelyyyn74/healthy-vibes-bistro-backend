package com.hvb.loyalty.service;

import com.hvb.loyalty.dto.UsuarioRequestDTO;
import com.hvb.loyalty.dto.UsuarioResponseDTO;
import com.hvb.loyalty.entity.Usuario;
import com.hvb.loyalty.repository.UsuarioRepository;
import com.hvb.loyalty.utils.UsuarioUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll().stream().map(UsuarioUtil::toResponse).toList();
    }

    public UsuarioResponseDTO obtener(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return UsuarioUtil.toResponse(u);
    }

    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        if (dto.getPassword() == null || dto.getPassword().isBlank())
            throw new RuntimeException("La contraseña es obligatoria");
        if (usuarioRepository.existsByCorreo(dto.getCorreo()))
            throw new RuntimeException("Ya existe un usuario con ese correo");

        Usuario u = new Usuario();
        u.setNombre(dto.getNombre());
        u.setCorreo(dto.getCorreo());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        u.setTelefono(dto.getTelefono());
        u.setRol(dto.getRol());
        return UsuarioUtil.toResponse(usuarioRepository.save(u));
    }

    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!u.getCorreo().equals(dto.getCorreo()) && usuarioRepository.existsByCorreo(dto.getCorreo()))
            throw new RuntimeException("Ya existe un usuario con ese correo");

        u.setNombre(dto.getNombre());
        u.setCorreo(dto.getCorreo());
        u.setTelefono(dto.getTelefono());
        u.setRol(dto.getRol());
        if (dto.getPassword() != null && !dto.getPassword().isBlank())
            u.setPassword(passwordEncoder.encode(dto.getPassword()));

        return UsuarioUtil.toResponse(usuarioRepository.save(u));
    }

    public UsuarioResponseDTO cambiarEstado(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        u.setActivo(!u.getActivo());
        return UsuarioUtil.toResponse(usuarioRepository.save(u));
    }
}