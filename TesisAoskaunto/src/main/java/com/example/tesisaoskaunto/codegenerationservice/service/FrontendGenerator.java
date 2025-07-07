package com.example.tesisaoskaunto.codegenerationservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.example.tesisaoskaunto.codegenerationservice.dto.ProjectData;
import com.example.tesisaoskaunto.codeservice.domain.models.Code;
import com.example.tesisaoskaunto.windowservice.domain.models.Window;

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

        Map<String, Object> projectContext = new HashMap<>();
        projectContext.put("projectName", projectData.getProject().getProjectName());

        // 2. Generar archivos de configuración
        generateFile(frontendDir, "package.json.vm", "package.json", projectContext);
        generateFile(frontendDir, "vite.config.js.vm", "vite.config.js", Collections.emptyMap());
        generateFile(frontendDir, "tailwind.config.js.vm", "tailwind.config.js", Collections.emptyMap());
        generateFile(frontendDir, "postcss.config.js.vm", "postcss.config.js", Collections.emptyMap());
        

        // 3. Generar archivos estáticos y principales
        generateFile(frontendDir.resolve("public"), "index.html.vm", "index.html", projectContext);
        generateFile(srcDir, "main.jsx.vm", "main.jsx", Collections.emptyMap());
        generateFile(srcDir, "index.css.vm", "index.css", Collections.emptyMap());

        // 4. Generar App.jsx (Enrutador)
        Map<String, Object> appContext = new HashMap<>();
        appContext.put("windows", projectData.getWindows());
        generateFile(srcDir, "App.jsx.vm", "App.jsx", appContext);

        // 5. Generar todos los componentes en su carpeta
        for (Code component : projectData.getComponents()) {
            String componentFileName = "Component" + component.getId() + ".jsx";
            Map<String, Object> componentContext = new HashMap<>();
            componentContext.put("component", component);
            generateFile(componentsDir, "Component.jsx.vm", componentFileName, componentContext);
        }

        // 6. Generar todas las páginas, pasando solo los componentes relevantes
        for (Window window : projectData.getWindows()) {
            List<Code> pageComponents = projectData.getComponents().stream()
                .filter(component -> component.getWindowId() != null && component.getWindowId().equals(window.getId()))
                .collect(Collectors.toList());

            String pageFileName = window.getWindowName().replace(" ", "") + ".jsx";
            Map<String, Object> pageContext = new HashMap<>();
            pageContext.put("window", window);
            pageContext.put("components", pageComponents);
            generateFile(pagesDir, "Page.jsx.vm", pageFileName, pageContext);
        }
    }

    private void generateFile(Path parentDir, String templateName, String outputFileName, Map<String, Object> context) throws IOException {
        String templatePath = "templates/code-generation/frontend/" + templateName;
        String content = templateService.processTemplate(templatePath, context);
        Files.writeString(parentDir.resolve(outputFileName), content);
    }

}