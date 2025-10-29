package com.reto.spring.llanogas.service;

import com.reto.spring.llanogas.dto.EvidenciaResponse;
import com.reto.spring.llanogas.entity.Evidencia;
import com.reto.spring.llanogas.entity.Reporte;
import com.reto.spring.llanogas.entity.Usuario;
import com.reto.spring.llanogas.exception.BadRequestException;
import com.reto.spring.llanogas.exception.ResourceNotFoundException;
import com.reto.spring.llanogas.repository.EvidenciaRepository;
import com.reto.spring.llanogas.repository.ReporteRepository;
import com.reto.spring.llanogas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvidenciaService {

    private final EvidenciaRepository evidenciaRepository;
    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    private Path getUploadPath() {
        return Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @Transactional
    public EvidenciaResponse subirEvidencia(Long reporteId, Long usuarioId, MultipartFile file) {
        Reporte reporte = reporteRepository.findById(reporteId)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado"));
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        try {
            // Crear directorio si no existe
            Path uploadPath = getUploadPath();
            Files.createDirectories(uploadPath);

            // Generar nombre único para el archivo
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String extension = "";
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = originalFilename.substring(dotIndex);
            }
            String filename = UUID.randomUUID().toString() + extension;

            // Guardar archivo
            Path targetLocation = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Crear registro en base de datos
            Evidencia evidencia = Evidencia.builder()
                    .nombreArchivo(originalFilename)
                    .rutaArchivo(filename)
                    .tipoArchivo(file.getContentType())
                    .tamano(file.getSize())
                    .reporte(reporte)
                    .subidoPor(usuario)
                    .build();

            evidencia = evidenciaRepository.save(evidencia);
            return mapToResponse(evidencia);

        } catch (IOException ex) {
            log.error("Error al guardar archivo: ", ex);
            throw new BadRequestException("Error al guardar el archivo: " + ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<EvidenciaResponse> obtenerPorReporte(Long reporteId) {
        return evidenciaRepository.findByReporteId(reporteId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EvidenciaResponse obtenerPorId(Long id) {
        Evidencia evidencia = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia no encontrada con id: " + id));
        return mapToResponse(evidencia);
    }

    @Transactional(readOnly = true)
    public Resource descargarEvidencia(Long id) {
        Evidencia evidencia = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia no encontrada con id: " + id));

        try {
            Path filePath = getUploadPath().resolve(evidencia.getRutaArchivo()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("Archivo no encontrado: " + evidencia.getNombreArchivo());
            }
        } catch (MalformedURLException ex) {
            log.error("Error al descargar archivo: ", ex);
            throw new BadRequestException("Error al descargar el archivo");
        }
    }

    @Transactional
    public void eliminar(Long id) {
        Evidencia evidencia = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia no encontrada con id: " + id));

        try {
            // Eliminar archivo físico
            Path filePath = getUploadPath().resolve(evidencia.getRutaArchivo()).normalize();
            Files.deleteIfExists(filePath);
            
            // Eliminar registro de base de datos
            evidenciaRepository.delete(evidencia);
        } catch (IOException ex) {
            log.error("Error al eliminar archivo: ", ex);
            throw new BadRequestException("Error al eliminar el archivo");
        }
    }

    private EvidenciaResponse mapToResponse(Evidencia evidencia) {
        return EvidenciaResponse.builder()
                .id(evidencia.getId())
                .nombreArchivo(evidencia.getNombreArchivo())
                .tipoArchivo(evidencia.getTipoArchivo())
                .tamano(evidencia.getTamano())
                .reporteId(evidencia.getReporte().getId())
                .subidoPorId(evidencia.getSubidoPor().getId())
                .subidoPorNombre(evidencia.getSubidoPor().getNombre() + " " + evidencia.getSubidoPor().getApellido())
                .creadoEn(evidencia.getCreadoEn())
                .build();
    }
}
