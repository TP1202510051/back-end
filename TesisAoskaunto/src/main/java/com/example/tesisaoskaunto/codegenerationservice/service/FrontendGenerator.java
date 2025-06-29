package com.example.tesisaoskaunto.codegenerationservice.service;

import org.springframework.stereotype.Service;

import com.example.tesisaoskaunto.codegenerationservice.dto.ProjectData;
import com.example.tesisaoskaunto.codeservice.domain.models.Code;
import com.example.tesisaoskaunto.windowservice.domain.models.Window;

import java.util.List;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FrontendGenerator {
    
    private final TemplateService templateService;

    public FrontendGenerator(TemplateService templateService) {
        this.templateService = templateService;
    }

    public void generate(Path targetDir, ProjectData projectData) throws IOException {
        // 1. Crear estructura de carpetas
        Path frontendDir = Files.createDirectory(targetDir.resolve("frontend"));
        Path srcDir = Files.createDirectory(frontendDir.resolve("src"));
        Path componentsDir = Files.createDirectory(srcDir.resolve("components"));
        Path pagesDir = Files.createDirectory(srcDir.resolve("pages"));
        Files.createDirectory(srcDir.resolve("services"));
        Files.createDirectory(frontendDir.resolve("public"));

        // 2. Generar archivos de configuración
        generateFile(frontendDir, "package.json.vm", "package.json", Map.of("projectName", projectData.getProject().getProjectName()));
        generateFile(frontendDir, "vite.config.js.vm", "vite.config.js", Collections.emptyMap());
        generateFile(frontendDir, "tailwind.config.js.vm", "tailwind.config.js", Collections.emptyMap());
        generateFile(frontendDir, "postcss.config.js.vm", "postcss.config.js", Collections.emptyMap());
        

        // 3. Generar archivos estáticos y principales
        generateFile(frontendDir.resolve("public"), "index.html.vm", "index.html", Map.of("projectName", projectData.getProject().getProjectName()));
        generateFile(srcDir, "main.jsx.vm", "main.jsx", Collections.emptyMap());
        generateFile(srcDir, "index.css.vm", "index.css", Collections.emptyMap());

        // 4. Generar App.jsx (Enrutador)
        generateFile(srcDir, "App.jsx.vm", "App.jsx", Map.of("windows", projectData.getWindows()));

        // 5. Generar todos los componentes en su carpeta
        for (Code component : projectData.getComponents()) {
            String componentFileName = "Component" + component.getId() + ".jsx";
            generateFile(componentsDir, "Component.jsx.vm", componentFileName, Map.of("component", component));
        }

        // 6. Generar todas las páginas, pasando solo los componentes relevantes
        for (Window window : projectData.getWindows()) {

            List<Code> pageComponents = projectData.getComponents().stream()
                .filter(component -> component.getWindowId() != null && component.getWindowId().equals(window.getId()))
                .collect(Collectors.toList());

            String pageFileName = window.getWindowName().replace(" ", "") + ".jsx";
            Map<String, Object> pageContext = Map.of(
                "window", window,
                "components", pageComponents // Se pasa la lista ya filtrada
            );
            generateFile(pagesDir, "Page.jsx.vm", pageFileName, pageContext);
        }
    }

    private void generateFile(Path parentDir, String templateName, String outputFileName, Map<String, Object> context) throws IOException {
        String templatePath = "templates/code-generation/frontend/" + templateName;
        String content = templateService.processTemplate(templatePath, context);
        Files.writeString(parentDir.resolve(outputFileName), content);
    }

}