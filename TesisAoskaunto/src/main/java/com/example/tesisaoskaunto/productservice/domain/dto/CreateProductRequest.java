package com.example.tesisaoskaunto.productservice.domain.dto;

import java.util.List;
import java.util.Set;

public class CreateProductRequest {
    private String name;
    private String description;
    private double price;
    private double discount;
    private String image;
    private String category;
    private Long projectId;
    private List<SizeDto> sizes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<SizeDto> getSizes() { return sizes; }
    public void setSizes(List<SizeDto> sizes) { this.sizes = sizes; }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
}
