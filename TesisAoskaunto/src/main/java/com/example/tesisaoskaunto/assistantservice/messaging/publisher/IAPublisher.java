package com.example.tesisaoskaunto.assistantservice.messaging.publisher;

import com.example.tesisaoskaunto.codeservice.service.CodeService;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;

@Service
public class IAPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public IAPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publishResponse(String message, Long projectId, String code) {
        var messagePayload = new HashMap<String, String>();
        messagePayload.put("message", message);
        messagePayload.put("code", code);
        System.out.println("🔔 Publicando WS en /topic/conversation/" + projectId + ": " + message);
        messagingTemplate.convertAndSend(
                "/topic/conversation/" + projectId,
                messagePayload
        );
    }
}
