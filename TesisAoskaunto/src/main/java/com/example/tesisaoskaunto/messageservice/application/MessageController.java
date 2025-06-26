package com.example.tesisaoskaunto.messageservice.application;

import com.example.tesisaoskaunto.messageservice.domain.dto.MessageRequest;
import com.example.tesisaoskaunto.messageservice.domain.dto.UpdateMessageRequest;
import com.example.tesisaoskaunto.messageservice.service.MessageService;
import com.example.tesisaoskaunto.messageservice.domain.models.Message;
import com.example.tesisaoskaunto.messageservice.infrastructure.repositories.MessagesRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessagesRepository messagesRepository;
    private final MessageService messageAssistant;

    public MessageController(MessagesRepository messagesRepository, MessageService messageAssistant) {
        this.messagesRepository = messagesRepository;
        this.messageAssistant = messageAssistant;
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<List<Message>> getMessagesByProjectId(@PathVariable Long projectId) {
        List<Message> messages = messagesRepository.findByProjectId(projectId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping
    public ResponseEntity<String> createMessage(@RequestBody MessageRequest request) {
        String generatedResponse = messageAssistant.saveMessageAndType(request.getMessage(), "prompt", request.getProjectId(), "");
        return ResponseEntity.ok(generatedResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateMessage(@PathVariable Long id, @RequestBody UpdateMessageRequest request) {
        Optional<Message> message = messagesRepository.findById(id);
        if (message.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Message msg = message.get();
        msg.setContent(request.getMessage());
        messagesRepository.save(msg);
        return ResponseEntity.ok("Message updated: " + msg.getContent());
    }
}