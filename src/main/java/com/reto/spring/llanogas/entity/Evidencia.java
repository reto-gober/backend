package com.reto.spring.llanogas.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "evidencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nombreArchivo;

    @Column(nullable = false, length = 500)
    private String rutaArchivo;

    @Column(length = 100)
    private String tipoArchivo;

    @Column
    private Long tamano;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporte_id", nullable = false)
    private Reporte reporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subido_por", nullable = false)
    private Usuario subidoPor;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creadoEn;
}
