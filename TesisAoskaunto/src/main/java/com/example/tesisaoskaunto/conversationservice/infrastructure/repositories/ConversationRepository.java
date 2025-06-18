package com.example.tesisaoskaunto.conversationservice.infrastructure.repositories;

import com.example.tesisaoskaunto.conversationservice.domain.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByUserId(Long userId);
}
