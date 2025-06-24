package com.example.tesisaoskaunto.windowservice.service;

import com.example.tesisaoskaunto.windowservice.domain.models.Window;
import com.example.tesisaoskaunto.windowservice.infrastructure.repositories.WindowRepository;
import org.springframework.stereotype.Service;

@Service
public class WindowService {

    private final WindowRepository windowRepository;

    public WindowService(WindowRepository windowRepository) {
        this.windowRepository = windowRepository;
    }

    public String saveWindow(Long projectId, String windowName) {
        Window window = new Window();
        window.setProjectId(projectId);
        window.setWindowName(windowName);
        var windowToSave = windowRepository.save(window);
        return "La ventana " + windowToSave.getWindowName() + " fue recibido para el proyecto " + windowToSave.getProjectId();
    }
}
