package com.reto.spring.llanogas.service;

import com.reto.spring.llanogas.dto.ReporteRequest;
import com.reto.spring.llanogas.dto.ReporteResponse;
import com.reto.spring.llanogas.entity.Entidad;
import com.reto.spring.llanogas.entity.Reporte;
import com.reto.spring.llanogas.entity.Usuario;
import com.reto.spring.llanogas.exception.ResourceNotFoundException;
import com.reto.spring.llanogas.repository.EntidadRepository;
import com.reto.spring.llanogas.repository.ReporteRepository;
import com.reto.spring.llanogas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final EntidadRepository entidadRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public ReporteResponse crear(ReporteRequest request) {
        Entidad entidad = entidadRepository.findById(request.getEntidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Entidad no encontrada"));
        
        Usuario responsable = usuarioRepository.findById(request.getResponsableId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario responsable no encontrado"));

        Reporte reporte = Reporte.builder()
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .entidad(entidad)
                .frecuencia(request.getFrecuencia())
                .formato(request.getFormato())
                .resolucion(request.getResolucion())
                .responsable(responsable)
                .fechaVencimiento(request.getFechaVencimiento())
                .estado(request.getEstado() != null ? request.getEstado() : Reporte.Estado.PENDIENTE)
                .build();

        reporte = reporteRepository.save(reporte);
        return mapToResponse(reporte);
    }

    @Transactional(readOnly = true)
    public Page<ReporteResponse> obtenerTodos(Pageable pageable) {
        return reporteRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ReporteResponse> obtenerPorEstado(Reporte.Estado estado, Pageable pageable) {
        return reporteRepository.findByEstado(estado, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ReporteResponse> obtenerPorResponsable(Long responsableId, Pageable pageable) {
        return reporteRepository.findByResponsableId(responsableId, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ReporteResponse> obtenerPorEntidad(Long entidadId, Pageable pageable) {
        return reporteRepository.findByEntidadId(entidadId, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public ReporteResponse obtenerPorId(Long id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con id: " + id));
        return mapToResponse(reporte);
    }

    @Transactional
    public ReporteResponse actualizar(Long id, ReporteRequest request) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con id: " + id));

        Entidad entidad = entidadRepository.findById(request.getEntidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Entidad no encontrada"));
        
        Usuario responsable = usuarioRepository.findById(request.getResponsableId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario responsable no encontrado"));

        reporte.setTitulo(request.getTitulo());
        reporte.setDescripcion(request.getDescripcion());
        reporte.setEntidad(entidad);
        reporte.setFrecuencia(request.getFrecuencia());
        reporte.setFormato(request.getFormato());
        reporte.setResolucion(request.getResolucion());
        reporte.setResponsable(responsable);
        reporte.setFechaVencimiento(request.getFechaVencimiento());
        
        if (request.getEstado() != null) {
            reporte.setEstado(request.getEstado());
            if (request.getEstado() == Reporte.Estado.ENVIADO && reporte.getFechaEnvio() == null) {
                reporte.setFechaEnvio(LocalDateTime.now());
            }
        }

        reporte = reporteRepository.save(reporte);
        return mapToResponse(reporte);
    }

    @Transactional
    public ReporteResponse cambiarEstado(Long id, Reporte.Estado nuevoEstado) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con id: " + id));

        reporte.setEstado(nuevoEstado);
        if (nuevoEstado == Reporte.Estado.ENVIADO && reporte.getFechaEnvio() == null) {
            reporte.setFechaEnvio(LocalDateTime.now());
        }

        reporte = reporteRepository.save(reporte);
        return mapToResponse(reporte);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!reporteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reporte no encontrado con id: " + id);
        }
        reporteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ReporteResponse> obtenerVencidos() {
        return reporteRepository.findVencidos(java.time.LocalDate.now()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ReporteResponse mapToResponse(Reporte reporte) {
        return ReporteResponse.builder()
                .id(reporte.getId())
                .titulo(reporte.getTitulo())
                .descripcion(reporte.getDescripcion())
                .entidadId(reporte.getEntidad().getId())
                .entidadNombre(reporte.getEntidad().getNombre())
                .frecuencia(reporte.getFrecuencia())
                .formato(reporte.getFormato())
                .resolucion(reporte.getResolucion())
                .responsableId(reporte.getResponsable().getId())
                .responsableNombre(reporte.getResponsable().getNombre() + " " + reporte.getResponsable().getApellido())
                .fechaVencimiento(reporte.getFechaVencimiento())
                .estado(reporte.getEstado())
                .fechaEnvio(reporte.getFechaEnvio())
                .creadoEn(reporte.getCreadoEn())
                .actualizadoEn(reporte.getActualizadoEn())
                .build();
    }
}
