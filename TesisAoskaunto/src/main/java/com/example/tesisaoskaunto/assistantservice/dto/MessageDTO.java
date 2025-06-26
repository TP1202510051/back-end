package com.example.tesisaoskaunto.assistantservice.dto;

import lombok.Data;

@Data
public class MessageDTO {
    private String message;
    private Long projectId;

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