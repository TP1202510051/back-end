package com.example.tesisaoskaunto.codeservice.application;

import com.example.tesisaoskaunto.codeservice.domain.dto.UpdateCodeRequest;
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

    @GetMapping("/project/{projectId}")
    public ResponseEntity<Optional<Code>> getConversationByProjectId(@PathVariable Long projectId) {
        Optional<Code> code = codeRepository.findByProjectId(projectId);
        return ResponseEntity.ok(code);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConversation(@PathVariable Long id) {
        if (!codeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        codeRepository.deleteById(id);
        return ResponseEntity.ok("Conversation deleted");
    }
}

