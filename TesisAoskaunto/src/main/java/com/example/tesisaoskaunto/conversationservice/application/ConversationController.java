package com.example.tesisaoskaunto.conversationservice.application;

import com.example.tesisaoskaunto.conversationservice.domain.dto.ConversationRequest;
import com.example.tesisaoskaunto.conversationservice.domain.dto.UpdateConversationRequest;
import com.example.tesisaoskaunto.conversationservice.service.ConversationService;
import com.example.tesisaoskaunto.conversationservice.domain.models.Conversation;
import com.example.tesisaoskaunto.conversationservice.infrastructure.repositories.ConversationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationRepository conversationRepository;
    private final ConversationService conversationAssistant;

    public ConversationController(ConversationRepository conversationRepository, ConversationService conversationAssistant) {
        this.conversationRepository = conversationRepository;
        this.conversationAssistant = conversationAssistant;
    }

    @PostMapping
    public ResponseEntity<String> createConversation(@RequestBody ConversationRequest request) {
        String generatedResponse = conversationAssistant.saveConversationInformation(request.getProjectId(), request.getName());
        return ResponseEntity.ok(generatedResponse);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Conversation>> getConversationByProjectId(@PathVariable Long projectId) {
        List<Conversation> conversations = conversationRepository.findByProjectId(projectId);
        return ResponseEntity.ok(conversations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateConversation(@PathVariable Long id, @RequestBody UpdateConversationRequest request) {
        Optional<Conversation> conversation = conversationRepository.findById(id);
        if (conversation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Conversation conv = conversation.get();
        conv.setConversationName(request.getName());
        conversationRepository.save(conv);
        return ResponseEntity.ok("Conversation updated: " + conv.getConversationName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConversation(@PathVariable Long id) {
        if (!conversationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        conversationRepository.deleteById(id);
        return ResponseEntity.ok("Conversation deleted");
    }
}

