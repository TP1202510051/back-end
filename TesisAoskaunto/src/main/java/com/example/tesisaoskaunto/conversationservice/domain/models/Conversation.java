package com.example.tesisaoskaunto.conversationservice.domain.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long userId;

    public String conversationName;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Conversation() {}

    public Conversation(Long userId, String conversationName) {
        this.userId = userId;
        this.conversationName = conversationName;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {return userId;}

    public String getConversationName() {return conversationName;}

    public String getCreatedAt() {
        return createdAt.toString();
    }

    public void setUserId(Long userId) {this.userId = userId;}

    public void setConversationName(String conversationName) {this.conversationName = conversationName;}
}
