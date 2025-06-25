package com.example.tesisaoskaunto.messageservice.service;

import com.example.tesisaoskaunto.messageservice.domain.models.Message;
import com.example.tesisaoskaunto.messageservice.infrastructure.repositories.MessagesRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessagesRepository messagesRepository;

    public MessageService(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    public String saveMessageAndType(String content, String type, Long projectId) {
        Message message = new Message();
        message.setContent(content);
        message.setType(type);
        message.setProjectId(projectId);
        var MessageToSave = messagesRepository.save(message);
        //Volver json y convertir el json en string
        return "El mensaje con el ID: " + MessageToSave.getId() + " fue recibido el " + MessageToSave.getCreatedAt() + " con el contenido: "+ MessageToSave.getContent() + " asociado al conversation: " + MessageToSave.getProjectId() + " el tipo de mesaje es " + MessageToSave.getType();
    }
}
