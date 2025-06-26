package com.example.tesisaoskaunto.assistantservice.service;

import com.example.tesisaoskaunto.assistantservice.ia.client.IAClient;
import com.example.tesisaoskaunto.assistantservice.messaging.publisher.IAPublisher;
import com.example.tesisaoskaunto.messageservice.domain.models.Message;
import com.example.tesisaoskaunto.messageservice.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageProcessingService {

    private static final Pattern FENCE =
            Pattern.compile("```[\\w]*\\n([\\s\\S]*?)```");

    private static final String SYSTEM_PROMPT = """
        Eres un asistente experto en generar código.
        Cuando el usuario pida código, responde con:
        1) Un bloque Markdown con el código (```java…``` o ```js…```)
        2) Una frase coloquial en español al final (por ejemplo: “Claro, ahorita lo modificamos.”)
        """;

    private final MessageService messageService;
    private final IAClient       iaClient;
    private final IAPublisher    iaPublisher;

    public MessageProcessingService(MessageService messageService,
                                    IAClient iaClient,
                                    IAPublisher iaPublisher) {
        this.messageService  = messageService;
        this.iaClient        = iaClient;
        this.iaPublisher     = iaPublisher;
    }

    @Transactional
    public void process(String incomingText, Long projectId) {
        try {
            messageService.saveMessageAndType(incomingText,
                    "prompt",
                    projectId,
                    "");

            List<Message> history = messageService
                    .getMessagesByProjectId(projectId);

            boolean hasSystem = history.stream()
                    .anyMatch(m -> "system".equals(m.getType()));
            if (!hasSystem) {
                messageService.saveMessageAndType(SYSTEM_PROMPT,
                        "system",
                        projectId,
                        "");
                history.add(0,
                        new Message(SYSTEM_PROMPT,
                                "",
                                Long.valueOf(0),
                                ""));
            }

            StringBuilder promptBuilder = new StringBuilder();
            for (Message msg : history) {
                String roleTag = switch (msg.getType()) {
                    case "system"   -> "SYSTEM";
                    case "prompt"   -> "USER";
                    case "response" -> "ASSISTANT";
                    default         -> msg.getType().toUpperCase();
                };
                promptBuilder
                        .append(roleTag)
                        .append(": ")
                        .append(msg.getContent())
                        .append("\n");
            }
            promptBuilder.append("ASSISTANT:");

            String fullResponse = iaClient
                    .generateResponse(promptBuilder.toString());

            Matcher matcher = FENCE.matcher(fullResponse);
            String code = "";
            if (matcher.find()) {
                code = matcher.group(1);
            }
            String text = fullResponse
                    .replaceAll(FENCE.pattern(), "")
                    .trim();

            iaPublisher.publishResponse(text, projectId);

            messageService.saveMessageAndType(text,
                    "response",
                    projectId,
                    code);

        } catch (Exception e) {
            System.err.println("❌ Error al procesar IA: " + e.getMessage());
        }
    }
}
