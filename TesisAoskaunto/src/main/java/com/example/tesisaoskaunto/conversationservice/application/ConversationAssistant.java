package com.example.tesisaoskaunto.conversationservice.application;

import com.example.tesisaoskaunto.conversationservice.domain.models.Conversation;
import com.example.tesisaoskaunto.conversationservice.infrastructure.repositories.ConversationRepository;

import org.springframework.stereotype.Service;

@Service
public class ConversationAssistant {

    private final ConversationRepository conversationRepository;

    public ConversationAssistant(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public String saveConversationInformation(Long userId, String name) {
        Conversation conversation = new Conversation();
        conversation.setUserId(userId);
        conversation.setConversationName(name);
        var conversationToSave = conversationRepository.save(conversation);
        return "La conversacion " + conversationToSave.getConversationName() + " fue recibida para el usuario " + conversationToSave.getUserId();
    }
}