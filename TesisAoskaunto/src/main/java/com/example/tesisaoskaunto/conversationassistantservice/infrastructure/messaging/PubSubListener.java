package com.example.tesisaoskaunto.conversationassistantservice.infrastructure.messaging;

import com.example.tesisaoskaunto.conversationassistantservice.application.ConversationAssistant;
import com.example.tesisaoskaunto.conversationassistantservice.domain.dto.MessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.cloud.pubsub.v1.AckReplyConsumer;

@Configuration
public class PubSubListener {

    private final ConversationAssistant conversationAssistantServiceassistant;
    private final ObjectMapper objectMapper;

    public PubSubListener(ConversationAssistant conversationAssistantServiceassistant) {
        this.conversationAssistantServiceassistant = conversationAssistantServiceassistant;
        this.objectMapper = new ObjectMapper();
        System.out.println("PubSubListener inicializado correctamente");
    }

    @PostConstruct
    public void subscribeToMessages() {
        System.out.println("Configurando StreamingPull...");

        String projectId = System.getProperty("PROJECT_ID");
        String subscriptionName = System.getProperty("SUBSCRIPTION");
        ProjectSubscriptionName subscription = ProjectSubscriptionName.of(projectId, subscriptionName);

        // Receptor de mensaje para procesar los mensajes
        MessageReceiver receiver = (PubsubMessage message, AckReplyConsumer consumer) -> {
            System.out.println("📥 Mensaje recibido: " + message.getData().toStringUtf8());
            try {
                String messageJson = message.getData().toStringUtf8();
                MessageDTO msg = objectMapper.readValue(messageJson, MessageDTO.class);
                System.out.println("📥 Mensaje procesado: " + msg.getMessage());

                conversationAssistantServiceassistant.saveMessageAndType(msg.getMessage(), msg.getType(), msg.getConversationId());
            } catch (Exception e) {
                System.err.println("❌ Error al procesar el mensaje: " + e.getMessage());
            } finally {
                consumer.ack();
            }
        };

        Subscriber subscriber = Subscriber.newBuilder(subscription, receiver).build();
        subscriber.startAsync().awaitRunning();
        System.out.println("✔️ Listening for messages...");
    }
}
