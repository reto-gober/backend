package com.reto.spring.llanogas.controller;

import com.reto.spring.llanogas.dto.EvidenciaResponse;
import com.reto.spring.llanogas.dto.MessageResponse;
import com.reto.spring.llanogas.security.UserDetailsImpl;
import com.reto.spring.llanogas.service.EvidenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Evidencias", description = "Gestión de evidencias y archivos de reportes")
@RestController
@RequestMapping("/api/evidencias")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class EvidenciaController {

    private final EvidenciaService evidenciaService;

    @Operation(summary = "Subir evidencia", description = "Sube un archivo como evidencia de un reporte")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/reporte/{reporteId}")
    public ResponseEntity<EvidenciaResponse> subirEvidencia(
            @PathVariable Long reporteId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        EvidenciaResponse response = evidenciaService.subirEvidencia(reporteId, userDetails.getId(), file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar evidencias de un reporte", description = "Obtiene todas las evidencias de un reporte")
    @GetMapping("/reporte/{reporteId}")
    public ResponseEntity<List<EvidenciaResponse>> listarPorReporte(@PathVariable Long reporteId) {
        return ResponseEntity.ok(evidenciaService.obtenerPorReporte(reporteId));
    }

    @Operation(summary = "Obtener evidencia por ID", description = "Obtiene los detalles de una evidencia específica")
    @GetMapping("/{id}")
    public ResponseEntity<EvidenciaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(evidenciaService.obtenerPorId(id));
    }

    @Operation(summary = "Descargar evidencia", description = "Descarga el archivo de una evidencia")
    @GetMapping("/{id}/descargar")
    public ResponseEntity<Resource> descargar(@PathVariable Long id) {
        Resource resource = evidenciaService.descargarEvidencia(id);
        EvidenciaResponse evidencia = evidenciaService.obtenerPorId(id);
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(evidencia.getTipoArchivo()))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + evidencia.getNombreArchivo() + "\"")
                .body(resource);
    }

    @Operation(summary = "Eliminar evidencia", description = "Elimina una evidencia del sistema")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> eliminar(@PathVariable Long id) {
        evidenciaService.eliminar(id);
        return ResponseEntity.ok(new MessageResponse("Evidencia eliminada exitosamente"));
    }
}
