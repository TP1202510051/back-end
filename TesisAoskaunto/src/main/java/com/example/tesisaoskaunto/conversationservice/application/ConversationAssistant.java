package com.example.tesisaoskaunto.conversationservice.application;

import com.example.tesisaoskaunto.conversationservice.domain.models.Conversation;
import com.example.tesisaoskaunto.messageservice.domain.models.Message;
import com.example.tesisaoskaunto.conversationservice.infrastructure.repositories.ConversationRepository;
import com.example.tesisaoskaunto.messageservice.infrastructure.repositories.MessagesRepository;
import org.springframework.stereotype.Service;

@Service
public class ConversationAssistant {

    private final ConversationRepository conversationRepository;
    private final MessagesRepository messagesRepository;

    public ConversationAssistant(ConversationRepository conversationRepository, MessagesRepository messagesRepository) {
        this.conversationRepository = conversationRepository;
        this.messagesRepository = messagesRepository;
    }

    public String saveMessageAndType(String content, String type, Long conversationId) {
        Message message = new Message();
        message.setContent(content);
        message.setType(type);
        message.setConversationId(conversationId);
        var MessageToSave = messagesRepository.save(message);
        //Volver json y convertir el json en string
        return "El mensaje con el ID: " + MessageToSave.getId() + " fue recibido el " + MessageToSave.getCreatedAt() + " con el contenido: "+ MessageToSave.getContent() + " asociado al conversation: " + MessageToSave.getConversationId() + " el tipo de mesaje es " + MessageToSave.getType();
    }

    public String saveConversationInformation(Long userId, String name) {
        Conversation conversation = new Conversation();
        conversation.setUserId(userId);
        conversation.setConversationName(name);
        var conversationToSave = conversationRepository.save(conversation);
        return "La conversacion " + conversationToSave.getConversationName() + " fue recibida para el usuario " + conversationToSave.getUserId();
    }
}