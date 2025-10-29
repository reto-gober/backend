package com.reto.spring.llanogas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 20)
    private NombreRol nombre;

    @Column(length = 200)
    private String descripcion;

    public enum NombreRol {
        ROLE_USER,
        ROLE_ADMIN
    }
}
