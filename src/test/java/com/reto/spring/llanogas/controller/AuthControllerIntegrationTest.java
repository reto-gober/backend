package com.reto.spring.llanogas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reto.spring.llanogas.dto.LoginRequest;
import com.reto.spring.llanogas.dto.RegistroRequest;
import com.reto.spring.llanogas.entity.Rol;
import com.reto.spring.llanogas.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RolRepository rolRepository;

    @BeforeEach
    void setUp() {
        if (rolRepository.count() == 0) {
            rolRepository.save(Rol.builder()
                    .nombre(Rol.NombreRol.ROLE_USER)
                    .descripcion("Usuario estándar")
                    .build());
            rolRepository.save(Rol.builder()
                    .nombre(Rol.NombreRol.ROLE_ADMIN)
                    .descripcion("Administrador")
                    .build());
        }
    }

    @Test
    void testRegistroUsuarioExitoso() throws Exception {
        RegistroRequest request = RegistroRequest.builder()
                .email("test@example.com")
                .nombre("Test")
                .apellido("User")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Usuario registrado exitosamente"));
    }

    @Test
    void testRegistroConEmailInvalido() throws Exception {
        RegistroRequest request = RegistroRequest.builder()
                .email("invalid-email")
                .nombre("Test")
                .apellido("User")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginExitoso() throws Exception {
        // Primero registrar usuario
        RegistroRequest registroRequest = RegistroRequest.builder()
                .email("login@example.com")
                .nombre("Login")
                .apellido("Test")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroRequest)));

        // Luego hacer login
        LoginRequest loginRequest = LoginRequest.builder()
                .email("login@example.com")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.email").value("login@example.com"));
    }
}
