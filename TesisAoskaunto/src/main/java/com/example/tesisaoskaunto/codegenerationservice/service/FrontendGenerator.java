package com.example.tesisaoskaunto.codegenerationservice.service;

import org.springframework.stereotype.Service;
import java.nio.file.Path;

@Service
public class FrontendGenerator {
    public void generate(Path targetDir, Object projectData) {
        System.out.println("Iniciando generación de Frontend en: " + targetDir);
        // Lógica para crear carpetas, configurar Tailwind y procesar plantillas .jsx.vm
    }
}