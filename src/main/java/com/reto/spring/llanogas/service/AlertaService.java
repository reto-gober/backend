package com.reto.spring.llanogas.service;

import com.reto.spring.llanogas.entity.Alerta;
import com.reto.spring.llanogas.entity.Reporte;
import com.reto.spring.llanogas.repository.AlertaRepository;
import com.reto.spring.llanogas.repository.ReporteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final ReporteRepository reporteRepository;
    private final EmailService emailService;

    @Scheduled(cron = "${app.scheduler.cron.alerts:0 0 8 * * ?}")
    @Transactional
    public void generarAlertasAutomaticas() {
        log.info("Iniciando generación de alertas automáticas");
        
        LocalDate hoy = LocalDate.now();
        LocalDate proximosDias = hoy.plusDays(7);

        // Buscar reportes próximos a vencer
        List<Reporte> reportesProximos = reporteRepository
                .findByFechaVencimientoBetween(hoy, proximosDias);

        for (Reporte reporte : reportesProximos) {
            if (reporte.getEstado() != Reporte.Estado.ENVIADO) {
                crearAlerta(reporte, Alerta.TipoAlerta.VENCIMIENTO_PROXIMO,
                        "El reporte '" + reporte.getTitulo() + "' vence el " + reporte.getFechaVencimiento());
            }
        }

        // Buscar reportes vencidos
        List<Reporte> reportesVencidos = reporteRepository.findVencidos(hoy);
        for (Reporte reporte : reportesVencidos) {
            crearAlerta(reporte, Alerta.TipoAlerta.VENCIDO,
                    "El reporte '" + reporte.getTitulo() + "' está vencido desde " + reporte.getFechaVencimiento());
        }

        log.info("Alertas automáticas generadas exitosamente");
    }

    @Transactional
    public void crearAlerta(Reporte reporte, Alerta.TipoAlerta tipo, String mensaje) {
        Alerta alerta = Alerta.builder()
                .reporte(reporte)
                .tipo(tipo)
                .mensaje(mensaje)
                .enviada(false)
                .build();
        
        alertaRepository.save(alerta);
    }

    @Scheduled(fixedDelay = 300000) // Cada 5 minutos
    @Transactional
    public void enviarAlertasPendientes() {
        log.info("Enviando alertas pendientes");
        
        List<Alerta> alertasPendientes = alertaRepository.findByEnviadaFalse();
        
        for (Alerta alerta : alertasPendientes) {
            try {
                String destinatario = alerta.getReporte().getResponsable().getEmail();
                String asunto = "Alerta de Reporte: " + alerta.getTipo();
                String cuerpo = alerta.getMensaje();
                
                emailService.enviarEmail(destinatario, asunto, cuerpo);
                
                alerta.setEnviada(true);
                alerta.setFechaEnvio(java.time.LocalDateTime.now());
                alertaRepository.save(alerta);
                
                log.info("Alerta enviada a: {}", destinatario);
            } catch (Exception e) {
                log.error("Error al enviar alerta {}: {}", alerta.getId(), e.getMessage());
            }
        }
    }

    @Transactional(readOnly = true)
    public List<Alerta> obtenerPorReporte(Long reporteId) {
        return alertaRepository.findByReporteId(reporteId);
    }
}
