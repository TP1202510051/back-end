package com.example.tesisaoskaunto.iaassistantservice.infrastructure.repositories;

import com.example.tesisaoskaunto.iaassistantservice.domain.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssistantRepository extends JpaRepository<Message, Long> {
}