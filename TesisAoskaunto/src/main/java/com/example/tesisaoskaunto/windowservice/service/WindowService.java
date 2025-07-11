package com.example.tesisaoskaunto.windowservice.service;

import com.example.tesisaoskaunto.categoryservice.domain.models.Category;
import com.example.tesisaoskaunto.windowservice.domain.models.Window;
import com.example.tesisaoskaunto.windowservice.infrastructure.repositories.WindowRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WindowService {

    private final WindowRepository windowRepository;

    public WindowService(WindowRepository windowRepository) {
        this.windowRepository = windowRepository;
    }

    public Window saveWindow(Long projectId, String windowName) {
        Window window = new Window();
        window.setProjectId(projectId);
        window.setWindowName(windowName);
        Window windowToSave = windowRepository.save(window);
        return windowToSave;
    }

    public List<Window> getWindowByProjectId(Long projectId) {
        return windowRepository.findByProjectId(projectId);
    }
}
