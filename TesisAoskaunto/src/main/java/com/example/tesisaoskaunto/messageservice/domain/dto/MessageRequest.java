package com.example.tesisaoskaunto.messageservice.domain.dto;

public class MessageRequest {
    private String message;
    private Long projectId;

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}