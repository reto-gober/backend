package com.reto.spring.llanogas.dto;

import com.reto.spring.llanogas.entity.Reporte;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 2, max = 200, message = "El título debe tener entre 2 y 200 caracteres")
    private String titulo;

    private String descripcion;

    @NotNull(message = "La entidad es obligatoria")
    private Long entidadId;

    @NotNull(message = "La frecuencia es obligatoria")
    private Reporte.Frecuencia frecuencia;

    @NotNull(message = "El formato es obligatorio")
    private Reporte.Formato formato;

    private String resolucion;

    @NotNull(message = "El responsable es obligatorio")
    private Long responsableId;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDate fechaVencimiento;

    private Reporte.Estado estado;
}
