package com.example.tesisaoskaunto.codeservice.application;

import com.example.tesisaoskaunto.codeservice.domain.models.Code;
import com.example.tesisaoskaunto.codeservice.infrastructure.repositories.CodeRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/codes")
public class CodeController {

    private final CodeRepository codeRepository;

    public CodeController(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    @GetMapping("/windows/{windowId}")
    public ResponseEntity<Optional<Code>> getCodeByWindowId(@PathVariable Long windowId) {
        Optional<Code> code = codeRepository.findByWindowId(windowId);
        return ResponseEntity.ok(code);
    }

    @GetMapping("/windows/latest/{windowId}")
    public ResponseEntity<Code> getLatestCodeByWindowId(@PathVariable Long windowId) {
        Code code = codeRepository.findTopByWindowIdOrderByCreatedAtDesc(windowId);
        return ResponseEntity.ok(code);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCode(@PathVariable Long id) {
        if (!codeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        codeRepository.deleteById(id);
        return ResponseEntity.ok("Conversation deleted");
    }
}

