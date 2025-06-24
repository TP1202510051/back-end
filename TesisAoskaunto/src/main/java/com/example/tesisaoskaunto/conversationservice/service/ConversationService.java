package com.example.tesisaoskaunto.conversationservice.service;

import com.example.tesisaoskaunto.conversationservice.domain.models.Conversation;
import com.example.tesisaoskaunto.conversationservice.infrastructure.repositories.ConversationRepository;

import org.springframework.stereotype.Service;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public String saveConversationInformation(Long projectId, String name) {
        Conversation conversation = new Conversation();
        conversation.setProjectId(projectId);
        conversation.setConversationName(name);
        var conversationToSave = conversationRepository.save(conversation);
        return "La conversacion " + conversationToSave.getConversationName() + " fue recibida para el projecto " + conversationToSave.getProjectId();
    }
}