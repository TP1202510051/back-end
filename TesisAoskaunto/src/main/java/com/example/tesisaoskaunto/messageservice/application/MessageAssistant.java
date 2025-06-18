package com.example.tesisaoskaunto.messageservice.application;

import com.example.tesisaoskaunto.messageservice.domain.models.Message;
import com.example.tesisaoskaunto.messageservice.infrastructure.repositories.MessagesRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageAssistant {

    private final MessagesRepository messagesRepository;

    public MessageAssistant(MessagesRepository messagesRepository) {
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
}
