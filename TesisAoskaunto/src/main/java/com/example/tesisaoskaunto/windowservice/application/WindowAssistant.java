package com.example.tesisaoskaunto.windowservice.application;

import com.example.tesisaoskaunto.windowservice.domain.models.Window;
import com.example.tesisaoskaunto.windowservice.infrastructure.repositories.WindowRepository;
import org.springframework.stereotype.Service;

@Service
public class WindowAssistant {

    private final WindowRepository windowRepository;

    public WindowAssistant(WindowRepository windowRepository) {
        this.windowRepository = windowRepository;
    }

    public String saveProject(Long userId, String windowName) {
        Window window = new Window();
        window.setProjectId(userId);
        window.setWindowName(windowName);
        var projectToSave = windowRepository.save(window);
        return "El projecto " + projectToSave.getWindowName() + " fue recibido para el proyecto " + projectToSave.getProjectId();
    }
}
