package com.example.tesisaoskaunto.messageservice.service;

import com.example.tesisaoskaunto.messageservice.domain.models.Message;
import com.example.tesisaoskaunto.messageservice.infrastructure.repositories.MessagesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessagesRepository messagesRepository;

    public MessageService(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    public String saveMessageAndType(String content, String type, Long projectId, String code) {
        Message message = new Message();
        message.setContent(content);
        message.setType(type);
        message.setProjectId(projectId);
        message.setCode(code);
        var MessageToSave = messagesRepository.save(message);
        return "El mensaje con el ID: " + MessageToSave.getId() + " fue recibido el " + MessageToSave.getCreatedAt() + " con el contenido: "+ MessageToSave.getContent() + " asociado al conversation: " + MessageToSave.getProjectId() + " el tipo de mesaje es " + MessageToSave.getType();
    }

    public List<Message> getMessagesByProjectId(Long projectId) {
        return messagesRepository
                .findByProjectIdOrderByCreatedAtAsc(projectId);
    }
}
