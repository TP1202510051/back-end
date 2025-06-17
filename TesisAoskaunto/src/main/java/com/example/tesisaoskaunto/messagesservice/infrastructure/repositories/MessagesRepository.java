package com.example.tesisaoskaunto.messagesservice.infrastructure.repositories;

import com.example.tesisaoskaunto.conversationassistantservice.domain.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessagesRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationId(String conversationId);
}
