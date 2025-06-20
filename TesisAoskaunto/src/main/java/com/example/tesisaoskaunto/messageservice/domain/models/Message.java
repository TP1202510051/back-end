package com.example.tesisaoskaunto.messageservice.domain.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    private String type;

    private Long conversationId;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Message() {}

    public Message(String content, String type, Long conversationId) {
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

    public Long getConversationId() {
        return conversationId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getCreatedAt() {
        return createdAt.toString();
    }
}