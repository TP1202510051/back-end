package com.example.tesisaoskaunto.assistantservice.messaging.publisher;

import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class IAPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public IAPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publishResponse(String message, Long projectId) {
        System.out.println("🔔 Publicando WS en /topic/conversation/" + projectId + ": " + message);
        messagingTemplate.convertAndSend(
                "/topic/conversation/" + projectId,
                message
        );
    }
}
