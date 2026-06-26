package com.hvb.loyalty.controller;

import com.hvb.loyalty.dto.PremioRequestDTO;
import com.hvb.loyalty.dto.PremioResponseDTO;
import com.hvb.loyalty.service.PremioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/premios")
public class PremioController {

    private final PremioService premioService;

    public PremioController(PremioService premioService) {
        this.premioService = premioService;
    }

    @GetMapping
    public ResponseEntity<List<PremioResponseDTO>> listar() {
        return ResponseEntity.ok(premioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(premioService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody PremioRequestDTO dto) {
        try {
            return ResponseEntity.status(201).body(premioService.crear(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody PremioRequestDTO dto) {
        try {
            return ResponseEntity.ok(premioService.actualizar(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            premioService.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Premio eliminado"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}