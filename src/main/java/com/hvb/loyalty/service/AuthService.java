package com.hvb.loyalty.service;

import com.hvb.loyalty.dto.LoginRequestDTO;
import com.hvb.loyalty.dto.LoginResponseDTO;
import com.hvb.loyalty.entity.Usuario;
import com.hvb.loyalty.repository.UsuarioRepository;
import com.hvb.loyalty.utils.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByUsuario(request.getUsuario())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        String token = jwtUtil.generarToken(usuario.getUsuario(), usuario.getId());
        return new LoginResponseDTO(token, usuario.getId(), usuario.getNombre(), usuario.getRol());
    }
}