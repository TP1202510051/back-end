package com.example.tesisaoskaunto.codegenerationservice.dto;

import com.example.tesisaoskaunto.codeservice.domain.models.Code;
import com.example.tesisaoskaunto.productservice.domain.models.Product;
import com.example.tesisaoskaunto.projectservice.domain.models.Project;
import com.example.tesisaoskaunto.windowservice.domain.models.Window;

import java.util.List;

public class ProjectData {

    private final Project project;
    private final List<Window> windows;
    private final List<Code> components;
    private final List<Product> products;

    public ProjectData(Project project, List<Window> windows, List<Code> components, List<Product> products) {
        this.project = project;
        this.windows = windows;
        this.components = components;
        this.products = products;
    }

    // Getters for all fields
    public Project getProject() { return project; }
    public List<Window> getWindows() { return windows; }
    public List<Code> getComponents() { return components; }
    public List<Product> getProducts() { return products; }
}