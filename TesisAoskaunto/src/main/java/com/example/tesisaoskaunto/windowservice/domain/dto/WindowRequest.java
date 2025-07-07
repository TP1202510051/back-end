package com.example.tesisaoskaunto.windowservice.domain.dto;

public class WindowRequest {
    private Long projectId;
    private String name;

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
