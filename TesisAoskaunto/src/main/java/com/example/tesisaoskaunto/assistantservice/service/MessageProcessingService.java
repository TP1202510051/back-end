package com.example.tesisaoskaunto.assistantservice.service;

import com.example.tesisaoskaunto.assistantservice.ia.client.IAClient;
import com.example.tesisaoskaunto.assistantservice.messaging.publisher.IAPublisher;
import com.example.tesisaoskaunto.codeservice.domain.models.Code;
import com.example.tesisaoskaunto.messageservice.domain.models.Message;
import com.example.tesisaoskaunto.messageservice.service.MessageService;
import com.example.tesisaoskaunto.codeservice.service.CodeService;
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
    Eres un asistente experto en generar fragmentos de JSX listos para react-jsx-parser.
    Cuando el usuario solicite un diseño de interfaz, responde solo y exactamente con:
    
    ```jsx
    [TU_JSX_SIN_IMPORTS_NI_EXPORTS_AQUÍ]
    [TU_FRASE_COLOQUIAL_EN_ESPAÑOL]
    
    No añadas nada más:
    
    Ni roles (“ASSISTANT:”, “SYSTEM:”).
    
    Ni explicaciones, comentarios ni imports/exports.
    
    Ni comillas alrededor de la frase.
    """;



    private final MessageService messageService;
    private final IAClient       iaClient;
    private final IAPublisher    iaPublisher;
    private final CodeService codeService;


    public MessageProcessingService(MessageService messageService, IAClient iaClient, IAPublisher iaPublisher, CodeService codeService) {
        this.messageService  = messageService;
        this.iaClient        = iaClient;
        this.iaPublisher     = iaPublisher;
        this.codeService  = codeService;
    }

    @Transactional
    public void process(String incomingText, Long projectId) {
        try {
            messageService.saveMessageAndType(incomingText,"prompt", projectId);

            List<Message> history = messageService.getMessagesByProjectId(projectId);
            boolean hasSystem = history.stream().anyMatch(m -> "system".equals(m.getType()));
            if (!hasSystem) {
                messageService.saveMessageAndType(SYSTEM_PROMPT, "system", projectId);
                history.add(0, new Message(SYSTEM_PROMPT, "", Long.valueOf(0)));
            }

            StringBuilder promptBuilder = new StringBuilder();

            for (Message msg : history) {
                String role = switch (msg.getType()) {
                    case "system"   -> "SYSTEM";
                    case "prompt"   -> "USER";
                    case "response" -> "ASSISTANT";
                    default         -> msg.getType().toUpperCase();
                };
                promptBuilder
                        .append(role)
                        .append(": ")
                        .append(msg.getContent())
                        .append("\n");
            }

            List<Code> codes = codeService.getCodesByProjectId(projectId);
            for (Code c : codes) {
                promptBuilder
                        .append("CODE_FOR_MESSAGE_ID:")
                        .append(c.getMessageId())
                        .append("\n```java\n")
                        .append(c.getCode())
                        .append("\n```\n");
            }
            promptBuilder.append("ASSISTANT:");

            String fullResponse = iaClient.generateResponse(promptBuilder.toString());

            Matcher m = FENCE.matcher(fullResponse);
            String code = m.find() ? m.group(1) : "";
            String text = fullResponse.replaceAll(FENCE.pattern(), "").trim();

            iaPublisher.publishResponse(text, projectId, code);
            Long messageId = messageService.saveMessageAndType(text, "response", projectId);

            String log = codeService.saveCode(projectId, code, messageId);
            System.out.println(log);

        } catch (Exception e) {
            System.err.println("❌ Error al procesar IA: " + e.getMessage());
        }
    }
}
