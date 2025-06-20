package com.example.tesisaoskaunto.assistantservice.dto;

public class IAResponseDTO {
    private String message;
    private Long conversationId;

    public IAResponseDTO(String message, Long conversationId) {
        this.message = message;
        this.conversationId = conversationId;
    }

    public String getMessage() {
        return message;
    }

    public Long getConversationId() { return conversationId; }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
}
