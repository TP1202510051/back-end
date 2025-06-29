package com.example.tesisaoskaunto.messageservice.domain.dto;

public class MessageRequest {
    private String message;
    private Long windowId;

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getWindowId() {
        return windowId;
    }

    public void setWindowId(Long windowId) {
        this.windowId = windowId;
    }
}