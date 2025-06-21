package com.example.tesisaoskaunto.assistantservice.service;

import com.example.tesisaoskaunto.assistantservice.ia.client.IAClient;
import com.example.tesisaoskaunto.assistantservice.messaging.publisher.IAPublisher;
import com.example.tesisaoskaunto.messageservice.application.MessageAssistant;
import org.springframework.stereotype.Service;

@Service
public class MessageProcessingService {

    private final MessageAssistant messageAssistant;
    private final IAClient iaClient;
    private final IAPublisher iaPublisher;

    public MessageProcessingService(MessageAssistant messageAssistant, IAClient iaClient, IAPublisher iaPublisher) {
        this.messageAssistant = messageAssistant;
        this.iaClient = iaClient;
        this.iaPublisher = iaPublisher;
    }

    public void process(String message, long conversationId) {
        try{
            messageAssistant.saveMessageAndType(message, "prompt", conversationId);
            //System.out.println("✅ Mensaje enviado al servicio correctamente");
            //System.out.println("Esperando respuesta...");

            String iaResponse = iaClient.generateResponse(message);
            //System.out.println("🧠 Respuesta IA: " + iaResponse);

            //Implementar websocket para enviar la respuesta al front
            iaPublisher.publishResponse(iaResponse, conversationId);

            messageAssistant.saveMessageAndType(iaResponse, "response", conversationId);
            //System.out.println("✅ Respuesta enviada al servicio correctamente");

        } catch (Exception e) {
            System.err.println("❌ Error al llamar a la IA: " + e.getMessage());
        }
    }
}