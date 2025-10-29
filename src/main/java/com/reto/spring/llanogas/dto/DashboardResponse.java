package com.reto.spring.llanogas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private Long totalReportes;
    private Long reportesPendientes;
    private Long reportesEnProgreso;
    private Long reportesEnviados;
    private Long reportesVencidos;
    private Double tasaCumplimiento;
}
