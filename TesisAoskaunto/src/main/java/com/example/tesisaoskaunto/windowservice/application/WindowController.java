package com.example.tesisaoskaunto.windowservice.application;

import com.example.tesisaoskaunto.windowservice.domain.dto.WindowRequest;
import com.example.tesisaoskaunto.windowservice.domain.dto.UpdateWindowRequest;
import com.example.tesisaoskaunto.windowservice.service.WindowService;
import com.example.tesisaoskaunto.windowservice.domain.models.Window;

import com.example.tesisaoskaunto.windowservice.infrastructure.repositories.WindowRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/windows")
public class WindowController {

    private final WindowRepository windowRepository;
    private final WindowService windowAssistant;

    public WindowController(WindowRepository windowRepository, WindowService windowAssistant) {
        this.windowRepository = windowRepository;
        this.windowAssistant = windowAssistant;
    }

    @PostMapping
    public ResponseEntity<String> createWindow(@RequestBody WindowRequest request) {
        String generatedResponse = windowAssistant.saveWindow(request.getProjectId(), request.getName());
        return ResponseEntity.ok(generatedResponse);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Window>> getWindowByProjectId(@PathVariable Long projectId) {
        List<Window> windows = windowRepository.findByProjectId(projectId);
        return ResponseEntity.ok(windows);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateWindow(@PathVariable Long id, @RequestBody UpdateWindowRequest request) {
        Optional<Window> window = windowRepository.findById(id);
        if (window.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Window win = window.get();
        win.setWindowName(request.getName());
        windowRepository.save(win);
        return ResponseEntity.ok("Window updated: " + win.getWindowName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWindow(@PathVariable Long id) {
        if (!windowRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        windowRepository.deleteById(id);
        return ResponseEntity.ok("Window deleted");
    }
}