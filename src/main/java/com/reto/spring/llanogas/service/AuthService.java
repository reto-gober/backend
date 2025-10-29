package com.reto.spring.llanogas.service;

import com.reto.spring.llanogas.dto.*;
import com.reto.spring.llanogas.entity.Rol;
import com.reto.spring.llanogas.entity.Usuario;
import com.reto.spring.llanogas.exception.BadRequestException;
import com.reto.spring.llanogas.repository.RolRepository;
import com.reto.spring.llanogas.repository.UsuarioRepository;
import com.reto.spring.llanogas.security.JwtUtils;
import com.reto.spring.llanogas.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toSet());

        Usuario usuario = usuarioRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        return JwtResponse.builder()
                .token(jwt)
                .tipo("Bearer")
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .roles(roles)
                .build();
    }

    @Transactional
    public MessageResponse registro(RegistroRequest registroRequest) {
        if (usuarioRepository.existsByEmail(registroRequest.getEmail())) {
            throw new BadRequestException("El email ya está en uso");
        }

        Usuario usuario = Usuario.builder()
                .email(registroRequest.getEmail())
                .nombre(registroRequest.getNombre())
                .apellido(registroRequest.getApellido())
                .password(passwordEncoder.encode(registroRequest.getPassword()))
                .activo(true)
                .build();

        Set<String> strRoles = registroRequest.getRoles();
        Set<Rol> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Rol userRole = rolRepository.findByNombre(Rol.NombreRol.ROLE_USER)
                    .orElseThrow(() -> new BadRequestException("Rol no encontrado"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    Rol adminRole = rolRepository.findByNombre(Rol.NombreRol.ROLE_ADMIN)
                            .orElseThrow(() -> new BadRequestException("Rol ADMIN no encontrado"));
                    roles.add(adminRole);
                } else {
                    Rol userRole = rolRepository.findByNombre(Rol.NombreRol.ROLE_USER)
                            .orElseThrow(() -> new BadRequestException("Rol USER no encontrado"));
                    roles.add(userRole);
                }
            });
        }

        usuario.setRoles(roles);
        usuarioRepository.save(usuario);

        return new MessageResponse("Usuario registrado exitosamente");
    }
}
