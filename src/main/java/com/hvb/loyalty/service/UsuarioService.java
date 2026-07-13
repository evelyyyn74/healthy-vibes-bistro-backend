package com.hvb.loyalty.service;

import com.hvb.loyalty.dto.UsuarioRequestDTO;
import com.hvb.loyalty.dto.UsuarioResponseDTO;
import com.hvb.loyalty.entity.Usuario;
import com.hvb.loyalty.repository.UsuarioRepository;
import com.hvb.loyalty.utils.UsuarioUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll().stream()
                .map(this::aResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return aResponse(usuario);
    }


    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria al crear un usuario");
        }

        // Limpia el teléfono (quita espacios) antes de validar y guardar
        if (dto.getTelefono() != null) {
            dto.setTelefono(dto.getTelefono().replaceAll("\\s", ""));
        }

        if (usuarioRepository.existsByUsuario(dto.getUsuario())) {
            throw new RuntimeException("Ya existe un usuario con ese nombre de usuario");
        }

        // Valida teléfono único (solo si mandaron teléfono)
        if (dto.getTelefono() != null && !dto.getTelefono().isBlank()
                && usuarioRepository.existsByTelefono(dto.getTelefono())) {
            throw new RuntimeException("Ya existe un usuario con ese teléfono");
        }

        Usuario usuario = new Usuario();
        UsuarioUtil.actualizarDatos(usuario, dto);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword())); // hashea
        usuario.setActivo(true);

        return aResponse(usuarioRepository.save(usuario));
    }


    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Limpia el teléfono antes de validar y guardar
        if (dto.getTelefono() != null) {
            dto.setTelefono(dto.getTelefono().replaceAll("\\s", ""));
        }

        if (dto.getUsuario() != null
                && !dto.getUsuario().equals(usuario.getUsuario())
                && usuarioRepository.existsByUsuario(dto.getUsuario())) {
            throw new RuntimeException("Ya existe un usuario con ese nombre de usuario");
        }

        // Valida teléfono único, pero permitiendo que conserve el suyo propio
        if (dto.getTelefono() != null && !dto.getTelefono().isBlank()
                && !dto.getTelefono().equals(usuario.getTelefono())
                && usuarioRepository.existsByTelefono(dto.getTelefono())) {
            throw new RuntimeException("Ya existe un usuario con ese teléfono");
        }

        UsuarioUtil.actualizarDatos(usuario, dto);

        // Solo cambia la contraseña si mandaron una nueva
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return aResponse(usuarioRepository.save(usuario));
    }

    public void cambiarEstado(Long id, Boolean activo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Si se va a DESACTIVAR un ADMIN, verifica que no sea el último admin activo
        if (Boolean.FALSE.equals(activo) && "ADMIN".equals(usuario.getRol())) {
            long adminsActivos = usuarioRepository.findAll().stream()
                    .filter(u -> "ADMIN".equals(u.getRol()) && Boolean.TRUE.equals(u.getActivo()))
                    .count();
            if (adminsActivos <= 1) {
                throw new RuntimeException("No puedes desactivar al último administrador activo.");
            }
        }

        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuarioRepository.delete(usuario);
    }

    private UsuarioResponseDTO aResponse(Usuario u) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setUsuario(u.getUsuario());
        dto.setRol(u.getRol());
        dto.setCorreo(u.getCorreo());
        dto.setTelefono(u.getTelefono());
        dto.setActivo(u.getActivo());
        dto.setFechaCreacion(u.getFechaCreacion());
        return dto;
    }
}