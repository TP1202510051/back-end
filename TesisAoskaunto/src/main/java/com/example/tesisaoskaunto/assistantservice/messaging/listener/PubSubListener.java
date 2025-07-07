package com.example.tesisaoskaunto.assistantservice.messaging.listener;

import com.example.tesisaoskaunto.assistantservice.dto.MessageDTO;
import com.example.tesisaoskaunto.assistantservice.messaging.handler.IncomingMessageHandler;
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

    private final IncomingMessageHandler handler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PubSubListener(IncomingMessageHandler handler) {
        this.handler = handler;
    }

    @PostConstruct
    public void subscribeToMessages() {
        //System.out.println("📡 Configurando suscripción Pub/Sub...");

        String projectId = System.getProperty("PROJECT_ID");
        String subscriptionName = System.getProperty("SUBSCRIPTION_PROMPT");

        ProjectSubscriptionName subscription = ProjectSubscriptionName.of(projectId, subscriptionName);

        MessageReceiver receiver = (PubsubMessage message, AckReplyConsumer consumer) -> {
            String messageJson = message.getData().toStringUtf8();
            //System.out.println("📥 Mensaje recibido: " + messageJson);
            try {
                MessageDTO dto = objectMapper.readValue(messageJson, MessageDTO.class);
                handler.handle(dto);
            } catch (Exception e) {
                System.err.println("❌ Error al deserializar el mensaje: " + e.getMessage());
            } finally {
                consumer.ack();
            }
        };

        Subscriber subscriber = Subscriber.newBuilder(subscription, receiver).build();
        subscriber.startAsync().awaitRunning();
        //System.out.println("✅ Listener activo para Pub/Sub");
    }
}
