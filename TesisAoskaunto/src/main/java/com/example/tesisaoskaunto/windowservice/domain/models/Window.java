package com.example.tesisaoskaunto.windowservice.domain.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "windows")
public class Window {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long projectId;

    public String windowName;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Window() {}

    public Window(Long projectId, String windowName) {
        this.projectId = projectId;
        this.windowName = windowName;
    }

    public Long getId() {
        return id;
    }

    public Long getProjectId() {return projectId;}

    public String getWindowName() {return windowName;}

    public String getCreatedAt() {
        return createdAt.toString();
    }

    public void setProjectId(Long projectId) {this.projectId = projectId;}

    public void setWindowName(String windowName) {this.windowName = windowName;}
}

