package com.example.tesisaoskaunto.conversationassistantservice.infrastructure.repositories;

import com.example.tesisaoskaunto.conversationassistantservice.domain.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
