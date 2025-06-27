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

    public Long saveMessageAndType(String content, String type, Long projectId) {
        Message message = new Message();
        message.setContent(content);
        message.setType(type);
        message.setProjectId(projectId);
        var MessageToSave = messagesRepository.save(message);
        return MessageToSave.getId();
    }

    public List<Message> getMessagesByProjectId(Long projectId) {
        return messagesRepository
                .findByProjectIdOrderByCreatedAtAsc(projectId);
    }
}
