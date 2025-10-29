package com.reto.spring.llanogas.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reportes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entidad_id", nullable = false)
    private Entidad entidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Frecuencia frecuencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Formato formato;

    @Column(length = 100)
    private String resolucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id", nullable = false)
    private Usuario responsable;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Estado estado = Estado.PENDIENTE;

    @Column
    private LocalDateTime fechaEnvio;

    @OneToMany(mappedBy = "reporte", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Evidencia> evidencias = new HashSet<>();

    @OneToMany(mappedBy = "reporte", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Alerta> alertas = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    private LocalDateTime actualizadoEn;

    public enum Frecuencia {
        MENSUAL,
        TRIMESTRAL,
        SEMESTRAL,
        ANUAL
    }

    public enum Formato {
        PDF,
        EXCEL,
        WORD,
        OTRO
    }

    public enum Estado {
        PENDIENTE,
        EN_PROGRESO,
        ENVIADO
    }
}
