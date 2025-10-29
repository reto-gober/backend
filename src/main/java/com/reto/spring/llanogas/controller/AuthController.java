package com.reto.spring.llanogas.controller;

import com.reto.spring.llanogas.dto.JwtResponse;
import com.reto.spring.llanogas.dto.LoginRequest;
import com.reto.spring.llanogas.dto.MessageResponse;
import com.reto.spring.llanogas.dto.RegistroRequest;
import com.reto.spring.llanogas.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticación", description = "Endpoints de autenticación y registro")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Iniciar sesión", description = "Autenticación de usuario y generación de token JWT")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @Operation(summary = "Registrar usuario", description = "Registro de nuevo usuario en el sistema")
    @PostMapping("/registro")
    public ResponseEntity<MessageResponse> registro(@Valid @RequestBody RegistroRequest registroRequest) {
        return ResponseEntity.ok(authService.registro(registroRequest));
    }
}
