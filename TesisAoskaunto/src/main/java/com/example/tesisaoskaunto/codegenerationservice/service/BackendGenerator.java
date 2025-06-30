package com.example.tesisaoskaunto.codegenerationservice.service;

import org.springframework.stereotype.Service;

import com.example.tesisaoskaunto.codegenerationservice.dto.ProjectData;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class BackendGenerator {

    private final TemplateService templateService;

    public BackendGenerator(TemplateService templateService) {
        this.templateService = templateService;
    }

    public void generate(Path targetDir, ProjectData projectData) throws IOException {
        // 1. Crear estructura de carpetas base
        Path backendDir = Files.createDirectory(targetDir.resolve("backend"));
        Path mainDir = backendDir.resolve("src/main");
        Path javaDir = mainDir.resolve("java");
        Path resourcesDir = mainDir.resolve("resources");

        Files.createDirectories(resourcesDir);
        
        String packageName = "com/tiendagenerada";
        Path packageDir = javaDir.resolve(packageName);
        Files.createDirectories(packageDir);

        // Crear sub-paquetes
        Files.createDirectory(packageDir.resolve("domain"));
        Files.createDirectory(packageDir.resolve("repository"));
        Files.createDirectory(packageDir.resolve("service"));
        Files.createDirectory(packageDir.resolve("web"));
        Files.createDirectory(packageDir.resolve("security"));

        // 2. Definir el contexto para las plantillas
        String projectName = projectData.getProject().getProjectName();
        String dbName = projectName.toLowerCase().replaceAll("[^a-zA-Z0-9]", "_");
        Map<String, Object> context = new HashMap<>();
        context.put("projectName", projectName);
        context.put("dbName", dbName);

        // 3. Generar archivos de configuración
        generateFile(backendDir, "pom.xml.vm", "pom.xml", context);
        generateFile(resourcesDir, "application.properties.vm", "application.properties", context);

        // 4. Generar clases Java
        String mainClassName = projectName.replace(" ", "") + "Application";
        Map<String, Object> mainClassContext = new HashMap<>();
        mainClassContext.put("mainClassName", mainClassName);
        generateFile(packageDir, "Application.java.vm", mainClassName + ".java", Map.of("mainClassName", mainClassContext));
        
        // Generar capa de Dominio
        generateFile(packageDir.resolve("domain"), "domain/Product.java.vm", "Product.java", Collections.emptyMap());
        generateFile(packageDir.resolve("domain"), "domain/Order.java.vm", "Order.java", Collections.emptyMap());

        // Generar capa de Repositorio
        generateFile(packageDir.resolve("repository"), "repository/ProductRepository.java.vm", "ProductRepository.java", Collections.emptyMap());
        generateFile(packageDir.resolve("repository"), "repository/OrderRepository.java.vm", "OrderRepository.java", Collections.emptyMap());
        
        // Generar capa de Seguridad
        generateFile(packageDir.resolve("security"), "security/SecurityConfig.java.vm", "SecurityConfig.java", Collections.emptyMap());
        generateFile(packageDir.resolve("security"), "security/ApiKeyFilter.java.vm", "ApiKeyFilter.java", Collections.emptyMap());
        
        // Generar capa de Servicio
        generateFile(packageDir.resolve("service"), "service/ProductService.java.vm", "ProductService.java", Collections.emptyMap());
        generateFile(packageDir.resolve("service"), "service/PaymentService.java.vm", "PaymentService.java", Collections.emptyMap());
        
        // Generar capa Web (Controladores)
        generateFile(packageDir.resolve("web"), "web/ProductController.java.vm", "ProductController.java", Collections.emptyMap());
        generateFile(packageDir.resolve("web"), "web/AdminProductController.java.vm", "AdminProductController.java", Collections.emptyMap());
        generateFile(packageDir.resolve("web"), "web/PaymentController.java.vm", "PaymentController.java", Collections.emptyMap());
    }

    private void generateFile(Path parentDir, String templateName, String outputFileName, Map<String, Object> context) throws IOException {
        String templatePath = "templates/code-generation/backend/" + templateName;
        String content = templateService.processTemplate(templatePath, context);
        Files.writeString(parentDir.resolve(outputFileName), content);
    }
}