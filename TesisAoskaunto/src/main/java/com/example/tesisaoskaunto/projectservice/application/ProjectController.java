package com.example.tesisaoskaunto.projectservice.application;

import com.example.tesisaoskaunto.projectservice.domain.dto.ProjectRequest;
import com.example.tesisaoskaunto.projectservice.domain.dto.UpdateProjectRequest;
import com.example.tesisaoskaunto.projectservice.service.ProjectService;
import com.example.tesisaoskaunto.projectservice.domain.models.Project;
import com.example.tesisaoskaunto.projectservice.infrastructure.repositories.ProjectRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    public ProjectController(ProjectRepository projectRepository, ProjectService projectService) {
        this.projectRepository = projectRepository;
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<Long> createProject(@RequestBody ProjectRequest request) {
        Long generatedResponse = projectService.saveProject(request.getUserId(), request.getName());
        System.out.println(generatedResponse);
        return ResponseEntity.ok(generatedResponse);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Project>> getProjectByUserId(@PathVariable String userId) {
        List<Project> projects = projectRepository.findByUserId(userId);
        return ResponseEntity.ok(projects);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProject(@PathVariable Long id, @RequestBody UpdateProjectRequest request) {
        Optional<Project> project = projectRepository.findById(id);

        if (project.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Project pro = project.get();
        pro.setProjectName(request.getName());
        projectRepository.save(pro);

        return ResponseEntity.ok("Project updated: " + pro.getProjectName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        if (!projectRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        projectRepository.deleteById(id);
        return ResponseEntity.ok("Project deleted");
    }
}
