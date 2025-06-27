package com.example.tesisaoskaunto.codeservice.domain.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "codes")
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long projectId;

    public Long messageId;

    @Lob
    @Column(columnDefinition = "TEXT")
    public String code;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Code() {}

    public Code(Long projectId, String code, Long messageId) {
        this.projectId = projectId;
        this.code = code;
        this.messageId = messageId;
    }

    public Long getId() {
        return id;
    }

    public Long getProjectId() {return projectId;}

    public String getCode() {return code;}

    public String getCreatedAt() {
        return createdAt.toString();
    }

    public void setProjectId(Long projectId) {this.projectId = projectId;}

    public void setCode(String code) {this.code = code;}

    public void setMessageId(Long messageId) {this.messageId = messageId;}

    public Long getMessageId() {return messageId;}
}
