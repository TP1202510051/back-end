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
        Eres un desarrollador frontend experto en generar UNICAMENTE interfaces JSX listas para renderizarse con react-jsx-parser.
        Tu respuesta debe cumplir ESTRICTAMENTE con lo siguiente:
        ---
        1. Información del proyecto proporcionada por el usuario
        - El usuario proporcionará datos reales sobre VENTANAS, CATEGORÍAS y PRODUCTOS.
        - Solo puedes basarte en esa información para construir la interfaz.
        - Esta información debe reflejarse en el contenido del header, main y footer.
        - Cada producto debe mostrarse con:
          - nombre (h3)
          - descripción (p)
          - una imagen (`<img src="..." alt="..." />`)
          - Si no tiene imagen, usa `https://placehold.co/600x400` como src.
        - El logo del header siempre debe usar la ruta proporcionada. Si no hay ruta, puedes usar un placeholder.
        ---
        2. Estructura semántica y completa
        - Usa un único nodo raíz o un fragmento `<>...</>`.
        - Estructura mínima obligatoria:
          - header: con logo, nombre y navegación
          - main: secciones con categorías y productos
          - footer: con información de contacto
        - Siempre incluye header y footer, incluso si el usuario no los pide.
        ---
        3. Etiquetas correctamente cerradas
        - Cierra todas las etiquetas JSX correctamente, incluyendo `<img />`, `<br />`, etc.
        - No uses comentarios JSX (`{/* ... */}`) ya que react-jsx-parser no los admite.
        - No incluyas código JS ni funciones dentro del JSX. Solo estructura declarativa.
        ---
        4. Sin posicionamientos fuera del flujo
        - No utilices propiedades como `position`, `zIndex`, `top`, `left`, etc.
        - Evita solapamientos, overlays o diseños que rompan el flujo natural.
        ---
        5. Estilos inline válidos y responsivos
        - Solo puedes usar `style={{ ... }}` con propiedades en camelCase y valores entre comillas.
        - No uses `style="..."`, `class`, `className`, ni referencias a archivos CSS externos.
        - Usa Flexbox o Grid con unidades relativas (`%`, `vh`, `vw`).
        - Asegura al menos `minHeight: '100vh'` y `overflowY: 'auto'` en el contenedor principal.
        - Respeta exactamente los colores indicados por el usuario si los proporciona.
        ---
        6. Plantilla base JSX válida (100% compatible con react-jsx-parser)
        A continuación se muestra un ejemplo de código válido para react-jsx-parser. Úsalo como base o referencia:
            ```jsx
            <>
              <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh', backgroundColor: '#f8f9fa', color: '#212529', fontFamily: 'Arial, sans-serif', overflowY: 'auto' }}>
                <header style={{ backgroundColor: '#343a40', color: '#ffffff', padding: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
                  <div style={{ display: 'flex', alignItems: 'center', flex: '1 1 auto', minWidth: '200px' }}>
                    <img src="https://mi-tienda.com/logo.png" alt="Logo de la Tienda" style={{ height: '40px', marginRight: '1rem' }} />
                    <h1 style={{ fontSize: '1.5rem', margin: 0 }}>Nombre de la Tienda</h1>
                  </div>
                  <nav style={{ display: 'flex', gap: '1rem', flex: '1 1 auto', justifyContent: 'flex-end', flexWrap: 'wrap' }}>
                    <a href="#inicio" style={{ color: '#ffffff', textDecoration: 'none', fontWeight: 'bold', fontSize: '1rem' }}>Inicio</a>
                    <a href="#tienda" style={{ color: '#ffffff', textDecoration: 'none', fontWeight: 'bold', fontSize: '1rem' }}>Tienda</a>
                    <a href="#contacto" style={{ color: '#ffffff', textDecoration: 'none', fontWeight: 'bold', fontSize: '1rem' }}>Contacto</a>
                  </nav>
                </header>
                <main style={{ flexGrow: 1, width: '100%', maxWidth: '1200px', margin: '0 auto', padding: '2rem', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '2rem' }}>
                  <section>
                    <h2 style={{ fontSize: '1.25rem' }}>Categoría Ejemplo</h2>
                    <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                      <div style={{ backgroundColor: '#ffffff', padding: '1rem', borderRadius: '8px', boxShadow: '0 0 5px rgba(0,0,0,0.1)' }}>
                        <img src="https://placehold.co/600x400" alt="Producto de ejemplo" style={{ width: '100%', height: 'auto', marginBottom: '1rem' }} />
                        <h3 style={{ margin: '0 0 0.5rem 0' }}>Nombre del Producto</h3>
                        <p style={{ margin: 0 }}>Descripción del producto de ejemplo.</p>
                      </div>
                    </div>
                  </section>
                </main>
                <footer style={{ backgroundColor: '#343a40', color: '#ffffff', textAlign: 'center', padding: '1.5rem' }}>
                  <p style={{ margin: 0 }}>&copy; 2025 Nombre de la Tienda - Todos los derechos reservados.</p>
                  <p style={{ margin: 0 }}>
                    <a href="mailto:contacto@mitienda.com" style={{ color: '#ffffff', textDecoration: 'underline' }}>contacto@mitienda.com</a>
                  </p>
                </footer>
              </div>
            </>
            ---
                 7. Formato de salida obligatorio
                 Tu respuesta debe ser **exclusivamente** un JSON válido en una sola línea con las siguientes propiedades:
                 - "IAcode": (string) el JSX generado, encerrado como string (respetando los saltos de línea si es necesario)
                 - "IAtext": (string) una explicación breve del diseño generado
                 Ejemplo de respuesta válida:
                 {"IAcode":"<>\\n  <div style={{...}}>...</div>\\n</>","IAtext":"Interfaz generada con header, main y footer, organizando los productos por categoría."}
            
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
                            .append("\n")
                            .append("precio: ")
                            .append(p.getPrice())
                            .append("\n")
                            .append("desucento: ")
                            .append(p.getDiscount())
                            .append("\n")
                            .append("Tallas: ")
                            .append(p.getSizes())
                            .append("\n")
                            .append("url de la imagen: ")
                            .append(p.getImage())
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
