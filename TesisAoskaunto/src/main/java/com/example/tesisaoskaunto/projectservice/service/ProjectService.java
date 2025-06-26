package com.example.tesisaoskaunto.projectservice.service;

import com.example.tesisaoskaunto.projectservice.domain.models.Project;
import com.example.tesisaoskaunto.projectservice.infrastructure.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Long saveProject(Long userId, String projectName) {
        Project project = new Project();
        project.setUserId(userId);
        project.setProjectName(projectName);
        var projectToSave = projectRepository.save(project);
        return projectToSave.getId();
    }
}
