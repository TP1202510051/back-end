package com.example.tesisaoskaunto.projectservice.application;

import com.example.tesisaoskaunto.projectservice.domain.models.Project;
import com.example.tesisaoskaunto.projectservice.infrastructure.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectAssistant {

    private final ProjectRepository projectRepository;

    public ProjectAssistant(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public String saveProject(Long userId, String projectName) {
        Project project = new Project();
        project.setUserId(userId);
        project.setProjectName(projectName);
        var projectToSave = projectRepository.save(project);
        return "El projecto " + projectToSave.getProjectName() + " fue recibido para el usuario " + projectToSave.getUserId();
    }
}
