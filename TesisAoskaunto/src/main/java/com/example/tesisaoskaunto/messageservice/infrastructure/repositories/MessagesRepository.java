package com.example.tesisaoskaunto.messageservice.infrastructure.repositories;

import com.example.tesisaoskaunto.messageservice.domain.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessagesRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationId(Long conversationId);
}
