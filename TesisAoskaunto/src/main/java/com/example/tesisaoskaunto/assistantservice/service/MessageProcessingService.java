package com.example.tesisaoskaunto.assistantservice.service;

import com.example.tesisaoskaunto.assistantservice.ia.client.IAClient;
import com.example.tesisaoskaunto.assistantservice.messaging.publisher.IAPublisher;
import com.example.tesisaoskaunto.codeservice.domain.models.Code;
import com.example.tesisaoskaunto.messageservice.domain.models.Message;
import com.example.tesisaoskaunto.messageservice.service.MessageService;
import com.example.tesisaoskaunto.codeservice.service.CodeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageProcessingService {

    private static final Pattern FENCE =
            Pattern.compile("```[\\w]*\\n([\\s\\S]*?)```");

    private static final String SYSTEM_PROMPT = """
        Eres un desarrollador experto en crear interfaces JSX listas para renderizarse con react-jsx-parser. \
        Debes cumplir estrictamente con lo siguiente:
        
        - Genera código JSX nativo, completo, limpio, bien estructurado y profesional, usando únicamente estilos inline mediante el prop `style` en cada elemento.
        - **No** emplees ninguna librería externa de estilos (ni Tailwind, Bootstrap o clases CSS externas).
        - Usa etiquetas semánticas claramente definidas (`<header>`, `<nav>`, `<main>`, `<section>`, `<footer>`).
        - Implementa atributos de accesibilidad adecuados (`aria-label`, `role`, `alt` en imágenes).
        - Crea interfaces completamente responsivas usando CSS puro inline (utiliza técnicas como Flexbox y Grid con unidades relativas: %, vw, vh).
        - Todos los textos deben tener buen contraste, con colores profesionales y sobrios.
        - Estructura claramente la interfaz en secciones, indicando visualmente áreas como barra de navegación, contenido principal, productos, pie de página, etc.
        - Incluye comentarios breves y descriptivos (en JSX) para identificar claramente cada sección o componente.
        - Asegúrate de que la interfaz simule una aplicación web real de tipo tienda virtual, lista para mejorar y desplegar posteriormente.
        - Mantén el código organizado, legible, fácil de entender y de modificar en futuras iteraciones.
        
        **FORMATO DE RESPUESTA (obligatorio):**
        
        Responde únicamente con un JSON válido de una sola línea, en este formato exacto:
        {"IAcode":"<TU_JSX_COMPLETO_CON_STYLE_INLINE_AQUÍ>","IAtext":"TU_FRASE_COLOQUIAL_EN_ESPAÑOL"}
        
        - `IAcode`: código JSX sin imports ni exports, con todos los estilos inline bien organizados y detallados.
        - `IAtext`: breve frase coloquial en español indicando que la interfaz está lista.
        
        **A CONTINUACIÓN SE INYECTARÁ EL CONTEXTO (historial de mensajes y códigos) Y EL ÚLTIMO PROMPT DEL USUARIO.**
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

    public static class JsonResponse {
        public String IAcode;
        public String IAtext;
    }

    private String stripFences(String raw) {
        raw = raw.trim();
        if (raw.startsWith("```")) {
            int nl = raw.indexOf('\n');
            raw = raw.substring(nl + 1);
            int last = raw.lastIndexOf("```");
            if (last > 0) raw = raw.substring(0, last);
        }
        return raw.replace("`", "").trim();
    }

    private static final int MAX_ATTEMPTS = 3;
    private static final String RETRY_PROMPT =
            "La respuesta anterior no contenía un JSON válido con 'code'. " +
                    "Por favor responde **únicamente** con un JSON de una sola línea " +
                    "con propiedades \"code\" y \"text\".";

    private JsonResponse callIAUntilValid(String fullPrompt) {
        ObjectMapper mapper = new ObjectMapper();
        String originalPrompt = fullPrompt;
        final String FIX_FORMAT =
                "\n\nPor favor, responde **únicamente** con un JSON de una sola línea " +
                        "con propiedades \"IAcode\" y \"IAtext\", y asegúrate de incluir todo el JSX pedido.";

        while (true) {
            String raw;
            try {
                raw = iaClient.generateResponse(originalPrompt);
            } catch (Exception e) {
                continue;
            }

            String candidate = stripFences(raw).replaceAll("\\\\'", "'");
            JsonResponse resp = null;
            try {
                resp = mapper.readValue(candidate, JsonResponse.class);
            } catch (JsonProcessingException e) {
                originalPrompt = originalPrompt + FIX_FORMAT;
                continue;
            }

            if (resp.IAcode != null && resp.IAcode.trim().length() > 10) {
                return resp;
            }
            originalPrompt = originalPrompt + FIX_FORMAT;
        }
    }

    @Transactional
    public void process(String incomingText, Long projectId) {
        try {
            messageService.saveMessageAndType(incomingText, "prompt", projectId);

            List<Message> history = messageService.getMessagesByProjectId(projectId);
            boolean hasSystem = history.stream().anyMatch(m -> "system".equals(m.getType()));
            if (!hasSystem) {
                messageService.saveMessageAndType(SYSTEM_PROMPT, "system", projectId);
                history.add(0, new Message(SYSTEM_PROMPT, "", 0L));
            }

            List<Code> codes = codeService.getCodesByProjectId(projectId);

            StringBuilder finalPrompt = new StringBuilder();
            finalPrompt.append(SYSTEM_PROMPT).append("\n\n");
            finalPrompt.append("# HISTORIAL DE MENSAJES (USER + ASSISTANT)\n");
            for (Message msg : history) {
                String role = switch (msg.getType()) {
                    case "system"   -> "SYSTEM";
                    case "prompt"   -> "USER";
                    case "response" -> "ASSISTANT";
                    default         -> msg.getType().toUpperCase();
                };
                finalPrompt
                        .append(role)
                        .append(": ")
                        .append(msg.getContent())
                        .append("\n");
            }

            finalPrompt.append("\n# HISTORIAL DE CÓDIGO GENERADO\n");
            for (Code c : codes) {
                finalPrompt
                        .append("CODE_FOR_MESSAGE_ID:")
                        .append(c.getMessageId())
                        .append("\n```java\n")
                        .append(c.getCode())
                        .append("\n```\n");
            }

            finalPrompt.append("\n# ÚLTIMA PETICIÓN DEL USUARIO\n");
            finalPrompt.append("USER: ").append(incomingText).append("\n");
            finalPrompt.append("\n# RESPONDE AHORA → ASSISTANT:\n");

            JsonResponse resp = callIAUntilValid(finalPrompt.toString());
            String code = resp.IAcode;
            String text = resp.IAtext;

            iaPublisher.publishResponse(text, projectId, code);
            Long messageId = messageService.saveMessageAndType(text, "response", projectId);
            String log = codeService.saveCode(projectId, code, messageId);
            System.out.println(log);

        } catch (Exception e) {
            System.err.println("❌ Error al procesar IA: " + e.getMessage());
        }
    }
}
