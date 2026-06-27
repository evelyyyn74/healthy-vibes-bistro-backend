package com.hvb.loyalty.controller;

import com.hvb.loyalty.dto.DashboardResponseDTO;
import com.hvb.loyalty.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> resumen() {
        return ResponseEntity.ok(dashboardService.obtenerResumen());
    }

    @GetMapping("/puntos-por-mes")
    public ResponseEntity<?> puntosPorMes() {
        return ResponseEntity.ok(dashboardService.puntosPorMes());
    }
}