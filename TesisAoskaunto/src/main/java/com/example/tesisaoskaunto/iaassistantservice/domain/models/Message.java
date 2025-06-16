package com.example.tesisaoskaunto.iaassistantservice.domain.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String type;

    private String conversationId;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Message() {}

    public Message(String content, String type, String conversationId) {
        this.content = content;
        this.type = type;
        this.conversationId = conversationId;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}