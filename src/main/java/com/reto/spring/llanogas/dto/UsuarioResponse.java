package com.reto.spring.llanogas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String email;
    private String nombre;
    private String apellido;
    private Boolean activo;
    private Set<String> roles;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}
