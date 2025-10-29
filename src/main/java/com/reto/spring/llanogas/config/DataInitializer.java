package com.reto.spring.llanogas.config;

import com.reto.spring.llanogas.entity.Rol;
import com.reto.spring.llanogas.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;

    @Override
    public void run(String... args) {
        // Inicializar roles si no existen
        if (rolRepository.count() == 0) {
            log.info("Inicializando roles del sistema...");
            
            Rol rolUser = Rol.builder()
                    .nombre(Rol.NombreRol.ROLE_USER)
                    .descripcion("Usuario estándar del sistema")
                    .build();
            
            Rol rolAdmin = Rol.builder()
                    .nombre(Rol.NombreRol.ROLE_ADMIN)
                    .descripcion("Administrador del sistema")
                    .build();
            
            rolRepository.save(rolUser);
            rolRepository.save(rolAdmin);
            
            log.info("Roles inicializados exitosamente");
        }
    }
}
