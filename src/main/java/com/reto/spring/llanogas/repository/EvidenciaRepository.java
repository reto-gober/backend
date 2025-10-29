package com.reto.spring.llanogas.repository;

import com.reto.spring.llanogas.entity.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvidenciaRepository extends JpaRepository<Evidencia, Long> {
    List<Evidencia> findByReporteId(Long reporteId);
}
