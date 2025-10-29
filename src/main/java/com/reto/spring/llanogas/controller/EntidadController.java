package com.reto.spring.llanogas.controller;

import com.reto.spring.llanogas.dto.EntidadRequest;
import com.reto.spring.llanogas.dto.EntidadResponse;
import com.reto.spring.llanogas.dto.MessageResponse;
import com.reto.spring.llanogas.service.EntidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Entidades", description = "Gestión de entidades de control")
@RestController
@RequestMapping("/api/entidades")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class EntidadController {

    private final EntidadService entidadService;

    @Operation(summary = "Crear entidad", description = "Crea una nueva entidad de control")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EntidadResponse> crear(@Valid @RequestBody EntidadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entidadService.crear(request));
    }

    @Operation(summary = "Listar todas las entidades", description = "Obtiene todas las entidades con paginación")
    @GetMapping
    public ResponseEntity<Page<EntidadResponse>> listarTodas(
            @PageableDefault(size = 10, sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(entidadService.obtenerTodas(pageable));
    }

    @Operation(summary = "Listar entidades activas", description = "Obtiene solo las entidades activas")
    @GetMapping("/activas")
    public ResponseEntity<Page<EntidadResponse>> listarActivas(
            @PageableDefault(size = 10, sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(entidadService.obtenerActivas(pageable));
    }

    @Operation(summary = "Obtener entidad por ID", description = "Obtiene los detalles de una entidad específica")
    @GetMapping("/{id}")
    public ResponseEntity<EntidadResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(entidadService.obtenerPorId(id));
    }

    @Operation(summary = "Actualizar entidad", description = "Actualiza los datos de una entidad existente")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntidadResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EntidadRequest request) {
        return ResponseEntity.ok(entidadService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar entidad", description = "Elimina una entidad del sistema")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> eliminar(@PathVariable Long id) {
        entidadService.eliminar(id);
        return ResponseEntity.ok(new MessageResponse("Entidad eliminada exitosamente"));
    }
}
