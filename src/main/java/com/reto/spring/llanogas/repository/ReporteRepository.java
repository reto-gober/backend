package com.reto.spring.llanogas.repository;

import com.reto.spring.llanogas.entity.Reporte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    Page<Reporte> findByEstado(Reporte.Estado estado, Pageable pageable);
    Page<Reporte> findByResponsableId(Long responsableId, Pageable pageable);
    Page<Reporte> findByEntidadId(Long entidadId, Pageable pageable);
    
    @Query("SELECT r FROM Reporte r WHERE r.fechaVencimiento BETWEEN :inicio AND :fin")
    List<Reporte> findByFechaVencimientoBetween(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
    
    @Query("SELECT r FROM Reporte r WHERE r.fechaVencimiento < :fecha AND r.estado != 'ENVIADO'")
    List<Reporte> findVencidos(@Param("fecha") LocalDate fecha);
    
    @Query("SELECT COUNT(r) FROM Reporte r WHERE r.estado = :estado AND YEAR(r.creadoEn) = :year AND MONTH(r.creadoEn) = :month")
    Long countByEstadoAndMonth(@Param("estado") Reporte.Estado estado, @Param("year") int year, @Param("month") int month);
}
