package com.reto.spring.llanogas.controller;

import com.reto.spring.llanogas.dto.DashboardResponse;
import com.reto.spring.llanogas.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Dashboard", description = "Estadísticas y métricas del sistema")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Obtener estadísticas", description = "Obtiene las estadísticas generales del sistema")
    @GetMapping("/estadisticas")
    public ResponseEntity<DashboardResponse> obtenerEstadisticas() {
        return ResponseEntity.ok(dashboardService.obtenerEstadisticas());
    }

    @Operation(summary = "Obtener tasa de cumplimiento", description = "Calcula la tasa de cumplimiento mensual")
    @GetMapping("/cumplimiento")
    public ResponseEntity<Double> obtenerTasaCumplimiento() {
        return ResponseEntity.ok(dashboardService.calcularTasaCumplimientoMensual());
    }
}
