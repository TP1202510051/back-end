package com.example.tesisaoskaunto.messageservice.domain.dto;

public class MessageRequest {
    private String message;
    private Long conversationId;

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
}