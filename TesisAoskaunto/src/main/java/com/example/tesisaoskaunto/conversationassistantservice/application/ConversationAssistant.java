package com.example.tesisaoskaunto.conversationassistantservice.application;

import com.example.tesisaoskaunto.conversationassistantservice.domain.models.Conversation;
import com.example.tesisaoskaunto.conversationassistantservice.domain.models.Message;
import com.example.tesisaoskaunto.conversationassistantservice.infrastructure.repositories.ConversationAssistantRepository;
import com.example.tesisaoskaunto.conversationassistantservice.infrastructure.repositories.ConversationRepository;
import org.springframework.stereotype.Service;

@Service
public class ConversationAssistant {

    private final ConversationAssistantRepository conversationAssistantRepository;
    private final ConversationRepository conversationRepository;

    public ConversationAssistant(ConversationAssistantRepository conversationAssistantRepository, ConversationRepository conversationRepository) {
        this.conversationAssistantRepository = conversationAssistantRepository;
        this.conversationRepository = conversationRepository;
    }

    public String saveMessageAndType(String content, String type, String conversationId) {
        Message message = new Message();
        message.setContent(content);
        message.setType(type);
        message.setConversationId(conversationId);
        conversationAssistantRepository.save(message);
        return "Tu " + type + " fue recibida: " + content;
    }

    public String saveConversationInformation(String userId, String name) {
        Conversation conversation = new Conversation();
        conversation.setUserId(userId);
        conversation.setConversationName(name);
        conversationRepository.save(conversation);
        return "La conversacion " + name + " fue recibida para el usuario " + userId;
    }
}