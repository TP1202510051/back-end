package com.example.tesisaoskaunto.codegenerationservice.application;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.tesisaoskaunto.codegenerationservice.service.CodeGenerationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/projects")
public class CodeGenerationController {
    private final CodeGenerationService codeGenerationService;

    public CodeGenerationController(CodeGenerationService codeGenerationService) {
        this.codeGenerationService = codeGenerationService;
    }

    @GetMapping("/{projectId}/download")
    public ResponseEntity<ByteArrayResource> downloadProject(@PathVariable Long projectId) {
        try {
            byte[] zipData = codeGenerationService.generateProjectZip(projectId);
            ByteArrayResource resource = new ByteArrayResource(zipData);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tienda-digital.zip");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(zipData.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            // Manejo de errores básico, se puede mejorar con un @ControllerAdvice
            return ResponseEntity.internalServerError().build();
        }
    }
}