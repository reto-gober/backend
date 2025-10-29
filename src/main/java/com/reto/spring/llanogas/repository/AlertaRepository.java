package com.reto.spring.llanogas.repository;

import com.reto.spring.llanogas.entity.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByEnviadaFalse();
    List<Alerta> findByReporteId(Long reporteId);
}
