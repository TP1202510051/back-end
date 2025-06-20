package com.example.tesisaoskaunto.projectservice.domain.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long userId;

    public String projectName;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Project() {}

    public Project(Long userId, String projectName) {
        this.userId = userId;
        this.projectName = projectName;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {return userId;}

    public String getProjectName() {return projectName;}

    public String getCreatedAt() {
        return createdAt.toString();
    }

    public void setUserId(Long userId) {this.userId = userId;}

    public void setProjectName(String projectName) {this.projectName = projectName;}
}

