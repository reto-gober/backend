package com.reto.spring.llanogas.service;

import com.reto.spring.llanogas.dto.DashboardResponse;
import com.reto.spring.llanogas.entity.Reporte;
import com.reto.spring.llanogas.repository.ReporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ReporteRepository reporteRepository;

    @Transactional(readOnly = true)
    public DashboardResponse obtenerEstadisticas() {
        long totalReportes = reporteRepository.count();
        long pendientes = reporteRepository.findByEstado(Reporte.Estado.PENDIENTE, null)
                .getTotalElements();
        long enProgreso = reporteRepository.findByEstado(Reporte.Estado.EN_PROGRESO, null)
                .getTotalElements();
        long enviados = reporteRepository.findByEstado(Reporte.Estado.ENVIADO, null)
                .getTotalElements();
        
        long vencidos = reporteRepository.findVencidos(LocalDate.now()).size();

        double tasaCumplimiento = calcularTasaCumplimientoMensual();

        return DashboardResponse.builder()
                .totalReportes(totalReportes)
                .reportesPendientes(pendientes)
                .reportesEnProgreso(enProgreso)
                .reportesEnviados(enviados)
                .reportesVencidos(vencidos)
                .tasaCumplimiento(tasaCumplimiento)
                .build();
    }

    @Transactional(readOnly = true)
    public Double calcularTasaCumplimientoMensual() {
        YearMonth mesActual = YearMonth.now();
        int year = mesActual.getYear();
        int month = mesActual.getMonthValue();

        Long totalMes = reporteRepository.findAll().stream()
                .filter(r -> {
                    if (r.getCreadoEn() == null) return false;
                    return r.getCreadoEn().getYear() == year && r.getCreadoEn().getMonthValue() == month;
                })
                .count();

        if (totalMes == 0) {
            return 100.0;
        }

        Long enviadosATiempo = reporteRepository.findAll().stream()
                .filter(r -> {
                    if (r.getCreadoEn() == null) return false;
                    if (r.getFechaEnvio() == null || r.getEstado() != Reporte.Estado.ENVIADO) return false;
                    boolean enMesActual = r.getCreadoEn().getYear() == year && r.getCreadoEn().getMonthValue() == month;
                    boolean aTiempo = !r.getFechaEnvio().toLocalDate().isAfter(r.getFechaVencimiento());
                    return enMesActual && aTiempo;
                })
                .count();

        return (enviadosATiempo.doubleValue() / totalMes.doubleValue()) * 100.0;
    }
}
