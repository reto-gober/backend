package com.reto.spring.llanogas.repository;

import com.reto.spring.llanogas.entity.Entidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntidadRepository extends JpaRepository<Entidad, Long> {
    Optional<Entidad> findByNombre(String nombre);
    Page<Entidad> findByActivoTrue(Pageable pageable);
}
