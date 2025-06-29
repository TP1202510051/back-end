package com.example.tesisaoskaunto.codegenerationservice.service;

import org.springframework.stereotype.Service;
import java.nio.file.Path;

@Service
public class BackendGenerator {
    public void generate(Path targetDir, Object projectData) {
        System.out.println("Iniciando generación de Backend en: " + targetDir);
        // Lógica para crear estructura, pom.xml, entidades, controladores, etc.
    }
}