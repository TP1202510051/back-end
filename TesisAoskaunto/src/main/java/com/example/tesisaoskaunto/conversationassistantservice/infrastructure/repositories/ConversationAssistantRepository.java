package com.example.tesisaoskaunto.conversationassistantservice.infrastructure.repositories;

import com.example.tesisaoskaunto.conversationassistantservice.domain.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationAssistantRepository extends JpaRepository<Message, Long> {
}