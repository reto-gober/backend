package com.reto.spring.llanogas.service;

import com.reto.spring.llanogas.dto.EntidadRequest;
import com.reto.spring.llanogas.dto.EntidadResponse;
import com.reto.spring.llanogas.entity.Entidad;
import com.reto.spring.llanogas.exception.BadRequestException;
import com.reto.spring.llanogas.exception.ResourceNotFoundException;
import com.reto.spring.llanogas.repository.EntidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EntidadService {

    private final EntidadRepository entidadRepository;

    @Transactional
    public EntidadResponse crear(EntidadRequest request) {
        if (entidadRepository.findByNombre(request.getNombre()).isPresent()) {
            throw new BadRequestException("Ya existe una entidad con ese nombre");
        }

        Entidad entidad = Entidad.builder()
                .nombre(request.getNombre())
                .codigo(request.getCodigo())
                .descripcion(request.getDescripcion())
                .activo(request.getActivo() != null ? request.getActivo() : true)
                .build();

        entidad = entidadRepository.save(entidad);
        return mapToResponse(entidad);
    }

    @Transactional(readOnly = true)
    public Page<EntidadResponse> obtenerTodas(Pageable pageable) {
        return entidadRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<EntidadResponse> obtenerActivas(Pageable pageable) {
        return entidadRepository.findByActivoTrue(pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public EntidadResponse obtenerPorId(Long id) {
        Entidad entidad = entidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entidad no encontrada con id: " + id));
        return mapToResponse(entidad);
    }

    @Transactional
    public EntidadResponse actualizar(Long id, EntidadRequest request) {
        Entidad entidad = entidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entidad no encontrada con id: " + id));

        if (!entidad.getNombre().equals(request.getNombre()) &&
                entidadRepository.findByNombre(request.getNombre()).isPresent()) {
            throw new BadRequestException("Ya existe una entidad con ese nombre");
        }

        entidad.setNombre(request.getNombre());
        entidad.setCodigo(request.getCodigo());
        entidad.setDescripcion(request.getDescripcion());
        if (request.getActivo() != null) {
            entidad.setActivo(request.getActivo());
        }

        entidad = entidadRepository.save(entidad);
        return mapToResponse(entidad);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!entidadRepository.existsById(id)) {
            throw new ResourceNotFoundException("Entidad no encontrada con id: " + id);
        }
        entidadRepository.deleteById(id);
    }

    private EntidadResponse mapToResponse(Entidad entidad) {
        return EntidadResponse.builder()
                .id(entidad.getId())
                .nombre(entidad.getNombre())
                .codigo(entidad.getCodigo())
                .descripcion(entidad.getDescripcion())
                .activo(entidad.getActivo())
                .creadoEn(entidad.getCreadoEn())
                .actualizadoEn(entidad.getActualizadoEn())
                .build();
    }
}
