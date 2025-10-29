package com.reto.spring.llanogas.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "alertas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporte_id", nullable = false)
    private Reporte reporte;

    @Column(nullable = false, length = 200)
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoAlerta tipo;

    @Column(nullable = false)
    @Builder.Default
    private Boolean enviada = false;

    @Column
    private LocalDateTime fechaEnvio;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creadoEn;

    public enum TipoAlerta {
        VENCIMIENTO_PROXIMO,
        VENCIDO,
        RECORDATORIO
    }
}
