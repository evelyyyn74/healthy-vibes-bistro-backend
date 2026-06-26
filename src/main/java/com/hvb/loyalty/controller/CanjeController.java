package com.hvb.loyalty.controller;

import com.hvb.loyalty.dto.CanjeRequestDTO;
import com.hvb.loyalty.dto.CanjeResponseDTO;
import com.hvb.loyalty.service.CanjeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/canjes")
public class CanjeController {

    private final CanjeService canjeService;

    public CanjeController(CanjeService canjeService) {
        this.canjeService = canjeService;
    }

    @GetMapping
    public ResponseEntity<List<CanjeResponseDTO>> listar() {
        return ResponseEntity.ok(canjeService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(canjeService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody CanjeRequestDTO dto) {
        try {
            return ResponseEntity.status(201).body(canjeService.crear(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            canjeService.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Canje eliminado"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}