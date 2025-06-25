package com.example.tesisaoskaunto.assistantservice.service;

import com.example.tesisaoskaunto.assistantservice.ia.client.IAClient;
import com.example.tesisaoskaunto.assistantservice.messaging.publisher.IAPublisher;
import com.example.tesisaoskaunto.messageservice.domain.models.Message;
import com.example.tesisaoskaunto.messageservice.infrastructure.repositories.MessagesRepository;
import com.example.tesisaoskaunto.messageservice.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class MessageProcessingService {

    private static final String SYSTEM_PROMPT = """
        Eres un asistente experto en generar código.
        Cuando el usuario pida código, responde con:
        1) Un bloque Markdown con el código (```java…``` o ```js…```)
        2) Una frase coloquial en español al final (por ejemplo: “Claro, ahorita lo modificamos.”)
        """;

    private final MessageService      messageService;
    private final IAClient            iaClient;
    private final IAPublisher         iaPublisher;
    private final MessagesRepository  messagesRepository;

    public MessageProcessingService(MessageService messageService,
                                    IAClient iaClient,
                                    IAPublisher iaPublisher,
                                    MessagesRepository messagesRepository) {
        this.messageService     = messageService;
        this.iaClient           = iaClient;
        this.iaPublisher        = iaPublisher;
        this.messagesRepository = messagesRepository;
    }

    @Transactional
    public void process(String incomingText, Long projectId) {
        try {
            messageService.saveMessageAndType(incomingText, "prompt", projectId);

            List<Message> history = messagesRepository
                    .findByProjectIdOrderByCreatedAtAsc(projectId);

            boolean hasSystem = history.stream()
                    .anyMatch(m -> "system".equals(m.getType()));
            if (!hasSystem) {
                messageService.saveMessageAndType(SYSTEM_PROMPT, "system", projectId);
                history.add(0, new Message(SYSTEM_PROMPT, "system", projectId));
            }

            StringBuilder promptBuilder = new StringBuilder();
            for (Message msg : history) {
                String roleTag;
                switch (msg.getType()) {
                    case "system"   -> roleTag = "SYSTEM";
                    case "prompt"   -> roleTag = "USER";
                    case "response" -> roleTag = "ASSISTANT";
                    default         -> roleTag = msg.getType().toUpperCase();
                }
                promptBuilder
                        .append(roleTag)
                        .append(": ")
                        .append(msg.getContent())
                        .append("\n");
            }
            promptBuilder.append("ASSISTANT:");

            String iaResponse = iaClient.generateResponse(promptBuilder.toString());

            iaPublisher.publishResponse(iaResponse, projectId);

            messageService.saveMessageAndType(iaResponse, "response", projectId);

        } catch (Exception e) {
            System.err.println("❌ Error al llamar a la IA: " + e.getMessage());
        }
    }
}