package com.example.tesisaoskaunto.codegenerationservice.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import com.example.tesisaoskaunto.projectservice.infrastructure.repositories.ProjectRepository;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;

@Service
public class CodeGenerationService {
    private final ProjectRepository projectRepository;
    private final FrontendGenerator frontendGenerator;
    private final BackendGenerator backendGenerator;
    private final ZipService zipService;

    public CodeGenerationService(ProjectRepository projectRepository, FrontendGenerator frontendGenerator, BackendGenerator backendGenerator, ZipService zipService) {
        this.projectRepository = projectRepository;
        this.frontendGenerator = frontendGenerator;
        this.backendGenerator = backendGenerator;
        this.zipService = zipService;
    }

    public byte[] generateProjectZip(Long projectId) throws IOException {
        // Validar que el proyecto existe
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado con ID: " + projectId));

        // TODO: Cargar todos los datos del proyecto (ventanas, componentes, productos, etc.)
        Object projectData = new Object(); // Placeholder para los datos completos

        Path tempDir = Files.createTempDirectory("tienda-generada-");
        try {
            // 1. Delegar generación
            frontendGenerator.generate(tempDir, projectData);
            backendGenerator.generate(tempDir, projectData);

            // 2. Empaquetar
            return zipService.createZip(tempDir);

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
