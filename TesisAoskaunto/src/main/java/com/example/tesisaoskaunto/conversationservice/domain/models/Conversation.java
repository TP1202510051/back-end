package com.example.tesisaoskaunto.conversationservice.domain.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long projectId;

    public String conversationName;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Conversation() {}

    public Conversation(Long projectId, String conversationName) {
        this.projectId = projectId;
        this.conversationName = conversationName;
    }

    public Long getId() {
        return id;
    }

    public Long getProjectId() {return projectId;}

    public String getConversationName() {return conversationName;}

    public String getCreatedAt() {
        return createdAt.toString();
    }

    public void setProjectId(Long projectId) {this.projectId = projectId;}

    public void setConversationName(String conversationName) {this.conversationName = conversationName;}
}
