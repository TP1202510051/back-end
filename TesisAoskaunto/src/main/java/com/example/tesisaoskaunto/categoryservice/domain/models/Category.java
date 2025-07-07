package com.example.tesisaoskaunto.categoryservice.domain.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long projectId;

    public String categoryName;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Category() {}

    public Category(Long projectId, String categoryName) {
        this.projectId = projectId;
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public Long getProjectId() {return projectId;}

    public String getCategoryName() {return categoryName;}

    public String getCreatedAt() {
        return createdAt.toString();
    }

    public void setProjectId(Long projectId) {this.projectId = projectId;}

    public void setCategoryName(String categoryName) {this.categoryName = categoryName;}
}

