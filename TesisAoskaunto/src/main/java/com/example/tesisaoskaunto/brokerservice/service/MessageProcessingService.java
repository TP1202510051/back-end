package com.example.tesisaoskaunto.brokerservice.service;

import com.example.tesisaoskaunto.messageservice.application.MessageAssistant;
import org.springframework.stereotype.Service;

@Service
public class MessageProcessingService {

    private final MessageAssistant messageAssistant;

    public MessageProcessingService(MessageAssistant messageAssistant) {
        this.messageAssistant = messageAssistant;
    }

    public void process(String message, String type, long conversationId) {
        messageAssistant.saveMessageAndType(message, type, conversationId);
        System.out.println("✅ Mensaje enviado al servicio correctamente");
    }
}