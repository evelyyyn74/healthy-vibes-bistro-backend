package com.hvb.loyalty.controller;

import com.hvb.loyalty.dto.ClienteRequestDTO;
import com.hvb.loyalty.dto.ClienteResponseDTO;
import com.hvb.loyalty.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<ClienteResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping("")
    public ClienteResponseDTO crear(@Valid @RequestBody ClienteRequestDTO dto) {
        return service.crear(dto);
    }

    @PutMapping("/{id}")
    public ClienteResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto) {
        return service.actualizar(id, dto);
    }

    @PutMapping("/{id}/reiniciar-puntos")
    public ClienteResponseDTO reiniciarPuntos(@PathVariable Long id) {
        return service.reiniciarPuntos(id);
    }

    @PutMapping("/{id}/estado")
    public ClienteResponseDTO cambiarEstado(@PathVariable Long id) {
        return service.cambiarEstado(id);
    }
}