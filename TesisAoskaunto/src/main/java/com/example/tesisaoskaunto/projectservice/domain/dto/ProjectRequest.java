package com.example.tesisaoskaunto.projectservice.domain.dto;

public class ProjectRequest {
    private String userId;
    private String name;

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
