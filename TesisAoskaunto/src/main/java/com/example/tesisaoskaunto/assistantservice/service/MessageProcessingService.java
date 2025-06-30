package com.example.tesisaoskaunto.assistantservice.service;

import com.example.tesisaoskaunto.assistantservice.ia.client.IAClient;
import com.example.tesisaoskaunto.assistantservice.messaging.publisher.IAPublisher;
import com.example.tesisaoskaunto.categoryservice.domain.models.Category;
import com.example.tesisaoskaunto.categoryservice.service.CategoryService;
import com.example.tesisaoskaunto.codeservice.domain.models.Code;
import com.example.tesisaoskaunto.messageservice.domain.models.Message;
import com.example.tesisaoskaunto.messageservice.service.MessageService;
import com.example.tesisaoskaunto.codeservice.service.CodeService;
import com.example.tesisaoskaunto.productservice.domain.models.Product;
import com.example.tesisaoskaunto.productservice.service.ProductService;
import com.example.tesisaoskaunto.windowservice.domain.models.Window;
import com.example.tesisaoskaunto.windowservice.service.WindowService;
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
    Eres un **desarrollador frontend experto** en generar **interfaces JSX** listas para renderizarse con **react-jsx-parser**.
    Tu respuesta debe cumplir **estrictamente** con lo siguiente:
    
    1. **Contexto real del proyecto**
       - Usa **solo** las VENTANAS, CATEGORÍAS y PRODUCTOS del proyecto actual.
       - Cada producto debe aparecer con su nombre, descripción y placeholder de imagen (usa https://placehold.co/600x400 si no hay ruta).
       - El logo en la cabecera debe usar siempre la ruta real proporcionada; no apliques el placeholder para el logo.
    
    2. **Estructura completa y semántica**
       - Un único elemento raíz o fragmento `<>…</>`.
       - Etiquetas semánticas: `<header>`, `<nav>`, `<main>`, `<section>`, `<footer>`.
       - Incluye siempre cabecera y pie de página, aunque el usuario no los solicite.
    
    3. **Balance y autocierre de etiquetas JSX**
       - **Obligatorio**: todas las etiquetas deben tener su cierre correspondiente.
       - Las imágenes **siempre** deben usarse como `<img src="…" alt="…" />`.
       - No generar `<img>` sin `/` al final ni tags sin cierre.
    
    4. **Sin posicionamientos fuera del flujo**
       - **Evita** `position: fixed|absolute|sticky` o propiedades que provoquen solapamientos o overflow.
    
    5. **Estilos inline limpios y responsivos**
       - Obligatorio: `style={{…}}` con objeto JS camelCase (no string CSS).
       - Usa Flexbox o Grid con unidades relativas (`%`, `vw`, `vh`).
       - La interfaz debe ocupar al menos `height: '100vh'` y permitir scroll interno si es necesario (`overflowY: 'auto'`).
       - Alto contraste en textos; si el usuario indica colores, respétalos.
    
    6. **Diseño de tienda virtual**
       - Cabecera con logo y menú (Inicio, Tienda, Contacto).
       - Sección principal: grid de productos agrupados por categoría, cada tarjeta con `<img src="…" alt="…" />`, nombre y descripción.
       - Footer con información de la empresa y enlaces de contacto.
    
    7. **Calidad y entrega**
       - Genera código **100% profesional**, limpio y listo para desplegar; el usuario solo deberá reemplazar rutas o datos.
       - No incluyas errores de sintaxis; debe parsearse sin fallos en `react-jsx-parser`.
    
    8. **Formato de respuesta**
       - **Único JSON** de una línea:
         ```json
         {"IAcode":"<JSX_COMPLETO_CON_STYLE_INLINE>","IAtext":"FRASE_COLOQUIAL_EN_ESPAÑOL"}
         ```
       - `IAcode`: puro JSX, sin escapes visibles y con todas las etiquetas correctamente cerradas.
       - `IAtext`: frase breve y amigable.
    
    **A CONTINUACIÓN** inyectaremos el historial y tu último mensaje. Responde **solo** con el JSON.
    """;

    private final MessageService messageService;
    private final IAClient       iaClient;
    private final IAPublisher    iaPublisher;
    private final CodeService codeService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final WindowService windowService;


    public MessageProcessingService(MessageService messageService, IAClient iaClient, IAPublisher iaPublisher, CodeService codeService, ProductService productService, CategoryService categoryService, WindowService windowService) {
        this.messageService  = messageService;
        this.iaClient        = iaClient;
        this.iaPublisher     = iaPublisher;
        this.codeService  = codeService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.windowService = windowService;
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
    public void process(String incomingText, Long projectId, Long windowId) {
        try {
            messageService.saveMessageAndType(incomingText, "prompt", windowId);

            List<Message> history = messageService.getMessagesByWindowId(windowId);
            boolean hasSystem = history.stream().anyMatch(m -> "system".equals(m.getType()));
            if (!hasSystem) {
                messageService.saveMessageAndType(SYSTEM_PROMPT, "system", windowId);
                history.add(0, new Message(SYSTEM_PROMPT, "", 0L));
            }

            List<Category> categories = categoryService.getCategoriesByProjectId(projectId);
            List<Window> windows = windowService.getWindowByProjectId(projectId);

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

            for (Window window : windows) {
                finalPrompt
                        .append("# VENTANA: ")
                        .append(window.getWindowName())
                        .append(" (ID: ")
                        .append(window.getId())
                        .append(")\n\n");

                // Historial de mensajes (USER + ASSISTANT) por ventana
                List<Message> windowMessages = messageService.getMessagesByWindowId(window.getId());
                finalPrompt.append("## HISTORIAL DE MENSAJES\n");
                for (Message msg : windowMessages) {
                    String role = switch (msg.getType()) {
                        case "system"   -> "SYSTEM";
                        case "prompt"   -> "USER";
                        case "response" -> "ASSISTANT";
                        default          -> msg.getType().toUpperCase();
                    };
                    finalPrompt
                            .append(role)
                            .append(": ")
                            .append(msg.getContent())
                            .append("\n");
                }
                finalPrompt.append("\n");

                // Historial de código generado por ventana
                List<Code> codes = codeService.getCodesByWindowId(window.getId());
                finalPrompt.append("## HISTORIAL DE CÓDIGO GENERADO\n");
                for (Code c : codes) {
                    finalPrompt
                            .append("CODE_FOR_MESSAGE_ID:")
                            .append(c.getMessageId())
                            .append("\n```java\n")
                            .append(c.getCode())
                            .append("\n```\n");
                }
                finalPrompt.append("\n");
            }

            for (Category category : categories) {
                finalPrompt
                        .append("# CATEGORÍA: ")
                        .append(category.getCategoryName())
                        .append(" (ID: ")
                        .append(category.getId())
                        .append(")\n\n");

                List<Product> products = productService.getProductsByCategoryId(category.getId());
                finalPrompt.append("## PRODUCTOS\n");
                for (Product p : products) {
                    finalPrompt
                            .append("- ")
                            .append(p.getName())
                            .append(": ")
                            .append(p.getDescription())
                            .append("\n");
                }
                finalPrompt.append("\n");
            }

            finalPrompt.append("\n# ÚLTIMA PETICIÓN DEL USUARIO\n");
            finalPrompt.append("USER: ").append(incomingText).append("\n");
            finalPrompt.append("\n# RESPONDE AHORA → ASSISTANT:\n");

            JsonResponse resp = callIAUntilValid(finalPrompt.toString());
            String code = resp.IAcode;
            String text = resp.IAtext;

            iaPublisher.publishResponse(text, windowId, code);
            Long messageId = messageService.saveMessageAndType(text, "response", windowId);
            String log = codeService.saveCode(windowId, code, messageId);
            System.out.println(log);

        } catch (Exception e) {
            System.err.println("❌ Error al procesar IA: " + e.getMessage());
        }
    }
}
