package com.example.tesisaoskaunto.iaassistantservice.application;

import com.example.tesisaoskaunto.iaassistantservice.domain.models.Message;
import com.example.tesisaoskaunto.iaassistantservice.infrastructure.repositories.AssistantRepository;
import org.springframework.stereotype.Service;

@Service
public class AssistantService {

    private final AssistantRepository assistantRepository;

    public AssistantService(AssistantRepository assistantRepository) {
        this.assistantRepository = assistantRepository;
    }

    public String saveMessageAndType(String content, Integer type, String conversationId) {
        Message message = new Message();
        message.setContent(content);
        message.setType(type);
        message.setConversationId(conversationId);
        assistantRepository.save(message);
        return "Tu " + type + " fue recibida: " + content;
    }
}