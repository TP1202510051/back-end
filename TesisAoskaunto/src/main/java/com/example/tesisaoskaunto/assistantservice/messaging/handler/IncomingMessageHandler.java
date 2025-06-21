package com.example.tesisaoskaunto.assistantservice.messaging.handler;

import com.example.tesisaoskaunto.assistantservice.dto.MessageDTO;
import com.example.tesisaoskaunto.assistantservice.service.MessageProcessingService;
import org.springframework.stereotype.Component;

@Component
public class IncomingMessageHandler {

    private final MessageProcessingService processingService;

    public IncomingMessageHandler(MessageProcessingService processingService) {
        this.processingService = processingService;
    }

    public void handle(MessageDTO dto) {
        try {
            long conversationId = Long.parseLong(dto.getConversationId());
            processingService.process(dto.getMessage(), conversationId);
        } catch (Exception e) {
            System.err.println("❌ Error al procesar el DTO: " + e.getMessage());
        }
    }
}
