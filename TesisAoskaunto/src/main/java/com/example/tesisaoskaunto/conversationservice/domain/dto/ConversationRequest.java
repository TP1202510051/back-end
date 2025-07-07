package com.example.tesisaoskaunto.conversationservice.domain.dto;

public class ConversationRequest {
    private Long projectId;
    private String name;

    // Getters and setters
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}