package com.hvb.loyalty.controller;

import com.hvb.loyalty.dto.TarjetaResponseDTO;
import com.hvb.loyalty.service.TarjetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tarjetas")
public class TarjetaController {

    private final TarjetaService tarjetaService;

    public TarjetaController(TarjetaService tarjetaService) {
        this.tarjetaService = tarjetaService;
    }

    @GetMapping
    public ResponseEntity<List<TarjetaResponseDTO>> listar() {
        return ResponseEntity.ok(tarjetaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(tarjetaService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/qr/{codigoQr}")
    public ResponseEntity<?> obtenerPorQr(@PathVariable String codigoQr) {
        try {
            return ResponseEntity.ok(tarjetaService.obtenerPorCodigoQr(codigoQr));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        try {
            tarjetaService.cambiarEstado(id, body.get("activo"));
            return ResponseEntity.ok(Map.of("mensaje", "Estado actualizado"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/cliente/{clienteId}/reiniciar")
    public ResponseEntity<?> reiniciarPuntos(@PathVariable Long clienteId) {
        try {
            tarjetaService.reiniciarPuntos(clienteId);
            return ResponseEntity.ok(Map.of("mensaje", "Puntos reiniciados"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }


}