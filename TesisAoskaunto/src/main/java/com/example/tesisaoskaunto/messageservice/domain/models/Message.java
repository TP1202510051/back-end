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

    private Long windowId;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Message() {}

    public Message(String content, String type, Long windowId) {
        this.content = content;
        this.type = type;
        this.windowId = windowId;
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

    public Long getWindowId() {
        return windowId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setWindowId(Long windowId) {
        this.windowId = windowId;
    }

    public String getCreatedAt() {
        return createdAt.toString();
    }
}