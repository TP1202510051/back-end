package com.example.tesisaoskaunto.codegenerationservice.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.example.tesisaoskaunto.codegenerationservice.dto.GeneratedProject;
import com.example.tesisaoskaunto.codegenerationservice.dto.ProjectData;
import com.example.tesisaoskaunto.codeservice.infrastructure.repositories.CodeRepository;
import com.example.tesisaoskaunto.productservice.infrastructure.repository.ProductRepository;
import com.example.tesisaoskaunto.projectservice.infrastructure.repositories.ProjectRepository;
import com.example.tesisaoskaunto.windowservice.infrastructure.repositories.WindowRepository;
import com.example.tesisaoskaunto.codeservice.domain.models.Code;
import com.example.tesisaoskaunto.windowservice.domain.models.Window;
import java.util.List;
import java.util.ArrayList;

import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.util.stream.Collectors;

@Service
public class CodeGenerationService {
    private final ProjectRepository projectRepository;
    private final FrontendGenerator frontendGenerator;
    private final BackendGenerator backendGenerator;
    private final ZipService zipService;
    private final WindowRepository windowRepository;
    private final CodeRepository codeRepository;
    private final ProductRepository productRepository;

    public CodeGenerationService(
        ProjectRepository projectRepository, 
        FrontendGenerator frontendGenerator, 
        BackendGenerator backendGenerator, 
        ZipService zipService, 
        WindowRepository windowRepository, 
        CodeRepository codeRepository, 
        ProductRepository productRepository) {
        this.projectRepository = projectRepository;
        this.frontendGenerator = frontendGenerator;
        this.backendGenerator = backendGenerator;
        this.zipService = zipService;
        this.windowRepository = windowRepository;
        this.codeRepository = codeRepository;
        this.productRepository = productRepository;
    }

    public GeneratedProject generateProjectZip(Long projectId) throws IOException {
        // Validar que el proyecto existe
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado con ID: " + projectId));

        var windows = windowRepository.findByProjectId(projectId);

        List<Code> allComponents = new ArrayList<>();

        if(!windows.isEmpty()) {
            List<Long> windowIds = windows.stream().map(Window::getId).collect(Collectors.toList());

            allComponents = codeRepository.findByWindowIdIn(windowIds);
        }
        var products = productRepository.findAll(); // TODO: Filtrar por projectId si es necesario

        ProjectData projectData = new ProjectData(project, windows, allComponents, products);

        String sanitizedProjectName = project.getProjectName().replaceAll("[^a-zA-Z0-9.-]", "_");
        String fileName = sanitizedProjectName + ".zip";        

        Path tempDir = Files.createTempDirectory("tienda-generada-");
        try {
            // 1. Delegar generación
            frontendGenerator.generate(tempDir, projectData);
            backendGenerator.generate(tempDir, projectData);

            // 2. Empaquetar
            byte[] zipData = zipService.createZip(tempDir);

            return new GeneratedProject(fileName, zipData);

        } finally {
            // 3. Limpiar siempre el directorio temporal
            deleteDirectory(tempDir);
        }
    }
    
    private void deleteDirectory(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
