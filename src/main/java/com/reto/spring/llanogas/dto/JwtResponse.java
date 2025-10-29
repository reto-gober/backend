package com.reto.spring.llanogas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String tipo = "Bearer";
    private Long id;
    private String email;
    private String nombre;
    private String apellido;
    private Set<String> roles;
}
