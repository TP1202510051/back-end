package com.example.tesisaoskaunto.assistantservice.dto;

import lombok.Data;

@Data
public class MessageDTO {
    private String message;
    private Long windowId;
    private Long projectId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getWindowId() {
        return windowId;
    }

    public Long getProjectId() {
        return projectId;
    }

}