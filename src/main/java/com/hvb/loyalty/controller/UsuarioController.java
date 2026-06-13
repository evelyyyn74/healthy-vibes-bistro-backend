package com.hvb.loyalty.controller;

import com.hvb.loyalty.dto.UsuarioRequestDTO;
import com.hvb.loyalty.dto.UsuarioResponseDTO;
import com.hvb.loyalty.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<UsuarioResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping("")
    public UsuarioResponseDTO crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        return service.crear(dto);
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO dto) {
        return service.actualizar(id, dto);
    }

    @PutMapping("/{id}/estado")
    public UsuarioResponseDTO cambiarEstado(@PathVariable Long id) {
        return service.cambiarEstado(id);
    }
}