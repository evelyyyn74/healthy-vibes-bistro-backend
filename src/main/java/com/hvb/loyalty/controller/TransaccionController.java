package com.hvb.loyalty.controller;

import com.hvb.loyalty.dto.TransaccionRequestDTO;
import com.hvb.loyalty.dto.TransaccionResponseDTO;
import com.hvb.loyalty.service.TransaccionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @GetMapping
    public ResponseEntity<List<TransaccionResponseDTO>> listar() {
        return ResponseEntity.ok(transaccionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(transaccionService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody TransaccionRequestDTO dto) {
        try {
            return ResponseEntity.status(201).body(transaccionService.crear(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            transaccionService.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Transacción eliminada"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}