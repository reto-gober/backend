package com.reto.spring.llanogas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvidenciaResponse {
    private Long id;
    private String nombreArchivo;
    private String tipoArchivo;
    private Long tamano;
    private Long reporteId;
    private Long subidoPorId;
    private String subidoPorNombre;
    private LocalDateTime creadoEn;
}
