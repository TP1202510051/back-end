package com.example.tesisaoskaunto.codegenerationservice.application;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tesisaoskaunto.codegenerationservice.dto.GeneratedProject;
import com.example.tesisaoskaunto.codegenerationservice.service.CodeGenerationService;



@RestController
@RequestMapping("/api/projects")
public class CodeGenerationController {
    private final CodeGenerationService codeGenerationService;

    public CodeGenerationController(CodeGenerationService codeGenerationService) {
        this.codeGenerationService = codeGenerationService;
    }

    @GetMapping("/{projectId}/download")
    public ResponseEntity<ByteArrayResource> downloadProject(@PathVariable Long projectId) throws IOException {
        GeneratedProject generatedProject = codeGenerationService.generateProjectZip(projectId);
        byte[] zipData = generatedProject.getFileContent();
        ByteArrayResource resource = new ByteArrayResource(zipData);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + generatedProject.getFileName());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(zipData.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}