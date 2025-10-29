package com.reto.spring.llanogas.controller;

import com.reto.spring.llanogas.dto.MessageResponse;
import com.reto.spring.llanogas.dto.ReporteRequest;
import com.reto.spring.llanogas.dto.ReporteResponse;
import com.reto.spring.llanogas.entity.Reporte;
import com.reto.spring.llanogas.service.ReporteService;
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

import java.util.List;

@Tag(name = "Reportes", description = "Gestión de reportes regulatorios")
@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReporteController {

    private final ReporteService reporteService;

    @Operation(summary = "Crear reporte", description = "Crea un nuevo reporte en el sistema")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<ReporteResponse> crear(@Valid @RequestBody ReporteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.crear(request));
    }

    @Operation(summary = "Listar todos los reportes", description = "Obtiene todos los reportes con paginación")
    @GetMapping
    public ResponseEntity<Page<ReporteResponse>> listarTodos(
            @PageableDefault(size = 10, sort = "fechaVencimiento", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(reporteService.obtenerTodos(pageable));
    }

    @Operation(summary = "Obtener reporte por ID", description = "Obtiene los detalles de un reporte específico")
    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.obtenerPorId(id));
    }

    @Operation(summary = "Listar reportes por estado", description = "Filtra reportes por su estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<ReporteResponse>> listarPorEstado(
            @PathVariable Reporte.Estado estado,
            @PageableDefault(size = 10, sort = "fechaVencimiento") Pageable pageable) {
        return ResponseEntity.ok(reporteService.obtenerPorEstado(estado, pageable));
    }

    @Operation(summary = "Listar reportes por responsable", description = "Obtiene reportes asignados a un usuario")
    @GetMapping("/responsable/{responsableId}")
    public ResponseEntity<Page<ReporteResponse>> listarPorResponsable(
            @PathVariable Long responsableId,
            @PageableDefault(size = 10, sort = "fechaVencimiento") Pageable pageable) {
        return ResponseEntity.ok(reporteService.obtenerPorResponsable(responsableId, pageable));
    }

    @Operation(summary = "Listar reportes por entidad", description = "Obtiene reportes de una entidad específica")
    @GetMapping("/entidad/{entidadId}")
    public ResponseEntity<Page<ReporteResponse>> listarPorEntidad(
            @PathVariable Long entidadId,
            @PageableDefault(size = 10, sort = "fechaVencimiento") Pageable pageable) {
        return ResponseEntity.ok(reporteService.obtenerPorEntidad(entidadId, pageable));
    }

    @Operation(summary = "Listar reportes vencidos", description = "Obtiene todos los reportes vencidos")
    @GetMapping("/vencidos")
    public ResponseEntity<List<ReporteResponse>> listarVencidos() {
        return ResponseEntity.ok(reporteService.obtenerVencidos());
    }

    @Operation(summary = "Actualizar reporte", description = "Actualiza los datos de un reporte existente")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<ReporteResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReporteRequest request) {
        return ResponseEntity.ok(reporteService.actualizar(id, request));
    }

    @Operation(summary = "Cambiar estado del reporte", description = "Actualiza el estado de un reporte")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ReporteResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Reporte.Estado estado) {
        return ResponseEntity.ok(reporteService.cambiarEstado(id, estado));
    }

    @Operation(summary = "Eliminar reporte", description = "Elimina un reporte del sistema")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> eliminar(@PathVariable Long id) {
        reporteService.eliminar(id);
        return ResponseEntity.ok(new MessageResponse("Reporte eliminado exitosamente"));
    }
}
