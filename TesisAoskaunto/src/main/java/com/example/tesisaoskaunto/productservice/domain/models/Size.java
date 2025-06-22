package com.example.tesisaoskaunto.productservice.domain.models;

import jakarta.persistence.*;

@Entity
@Table(name = "size")
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean isActive;

    public Size(String name, boolean isActive) {
        this.name = name;
        this.isActive = isActive;
    }

    public Size() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }


}
