package com.hvb.loyalty.controller;

import com.hvb.loyalty.dto.NivelRequestDTO;
import com.hvb.loyalty.dto.NivelResponseDTO;
import com.hvb.loyalty.service.NivelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/niveles")
public class NivelController {

    private final NivelService nivelService;

    public NivelController(NivelService nivelService) {
        this.nivelService = nivelService;
    }

    @GetMapping
    public ResponseEntity<List<NivelResponseDTO>> listar() {
        return ResponseEntity.ok(nivelService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(nivelService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody NivelRequestDTO dto) {
        try {
            return ResponseEntity.status(201).body(nivelService.crear(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody NivelRequestDTO dto) {
        try {
            return ResponseEntity.ok(nivelService.actualizar(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            nivelService.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Nivel eliminado"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}