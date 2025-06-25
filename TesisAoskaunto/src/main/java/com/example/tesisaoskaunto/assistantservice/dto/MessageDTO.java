package com.example.tesisaoskaunto.assistantservice.dto;

import lombok.Data;

@Data
public class MessageDTO {
    private String message;
    private String projectId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}