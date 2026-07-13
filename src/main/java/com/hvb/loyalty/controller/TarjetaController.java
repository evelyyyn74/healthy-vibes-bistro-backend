package com.hvb.loyalty.controller;

import com.hvb.loyalty.dto.TarjetaResponseDTO;
import com.hvb.loyalty.entity.Tarjeta;
import com.hvb.loyalty.repository.TarjetaRepository;
import com.hvb.loyalty.service.GoogleWalletService;
import com.hvb.loyalty.service.TarjetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tarjetas")
public class TarjetaController {

    private final TarjetaService tarjetaService;
    private final GoogleWalletService googleWalletService;
    private final TarjetaRepository tarjetaRepository;

    public TarjetaController(TarjetaService tarjetaService,
                             GoogleWalletService googleWalletService,
                             TarjetaRepository tarjetaRepository) {
        this.tarjetaService = tarjetaService;
        this.googleWalletService = googleWalletService;
        this.tarjetaRepository = tarjetaRepository;
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

    @GetMapping("/qr/{codigoQr}/google-wallet")
    public ResponseEntity<?> linkGoogleWallet(@PathVariable String codigoQr) {
        try {
            // Busca la tarjeta para sacar nombre y puntos
            var tarjeta = tarjetaService.obtenerPorCodigoQr(codigoQr);
            String nombreCliente = tarjeta.getClienteNombre() != null ? tarjeta.getClienteNombre() : "Cliente";
            int puntos = tarjeta.getPuntosAcumulados() != null ? tarjeta.getPuntosAcumulados() : 0;
            String nivelNombre = tarjeta.getNivelNombre() != null ? tarjeta.getNivelNombre() : "Nuevo";
            int recompensas = tarjeta.getRecompensasObtenidas() != null ? tarjeta.getRecompensasObtenidas() : 0;

            String link = googleWalletService.generarLinkParaCliente(codigoQr, nombreCliente, puntos, nivelNombre, recompensas);

            // Marca que el cliente sí llegó al paso de generar el pase (cancela cualquier recordatorio pendiente)
            tarjetaRepository.findByCodigoQr(codigoQr).ifPresent(t -> {
                t.setWalletAgregado(true);
                tarjetaRepository.save(t);
            });

            return ResponseEntity.ok(java.util.Map.of("link", link));
        } catch (Exception e) {
            System.err.println("Error generando link de Google Wallet: " + e.getMessage());
            return ResponseEntity.status(500).body(java.util.Map.of("error", "No se pudo generar el pase"));
        }
    }

    /**
     * Se llama cuando el cliente le da "Ahora no" al agregar su Wallet.
     * Registra el momento para que el scheduler le mande un recordatorio más tarde.
     */
    @PatchMapping("/qr/{codigoQr}/posponer-wallet")
    public ResponseEntity<?> posponerWallet(@PathVariable String codigoQr) {
        Tarjeta tarjeta = tarjetaRepository.findByCodigoQr(codigoQr)
                .orElse(null);

        if (tarjeta == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Tarjeta no encontrada"));
        }

        if (Boolean.TRUE.equals(tarjeta.getWalletAgregado())) {
            // Ya la agregó, no hace falta registrar que la pospuso
            return ResponseEntity.ok(Map.of("mensaje", "El cliente ya agregó su Wallet"));
        }

        tarjeta.setWalletPospuestoEn(LocalDateTime.now());
        tarjetaRepository.save(tarjeta);
        return ResponseEntity.ok(Map.of("mensaje", "Registrado, se le recordará más tarde"));
    }
}