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

    private Long projectId;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Message() {}

    public Message(String content, String type, Long projectId) {
        this.content = content;
        this.type = type;
        this.projectId = projectId;
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

    public Long getProjectId() {
        return projectId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getCreatedAt() {
        return createdAt.toString();
    }
}