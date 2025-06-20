package com.example.tesisaoskaunto.assistantservice.messaging.publisher;

import org.springframework.stereotype.Service;

@Service
public class IAPublisher {

    public void publishResponse(String message, Long conversationId) {
        System.out.println("Lo que se va a enviar en el websocket: " + message + " " + conversationId);
    }
}
