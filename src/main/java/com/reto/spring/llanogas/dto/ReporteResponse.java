package com.reto.spring.llanogas.dto;

import com.reto.spring.llanogas.entity.Reporte;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteResponse {
    private Long id;
    private String titulo;
    private String descripcion;
    private Long entidadId;
    private String entidadNombre;
    private Reporte.Frecuencia frecuencia;
    private Reporte.Formato formato;
    private String resolucion;
    private Long responsableId;
    private String responsableNombre;
    private LocalDate fechaVencimiento;
    private Reporte.Estado estado;
    private LocalDateTime fechaEnvio;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}
