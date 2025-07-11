package com.example.tesisaoskaunto.categoryservice.service;

import com.example.tesisaoskaunto.categoryservice.domain.models.Category;
import com.example.tesisaoskaunto.categoryservice.infrastructure.repositories.CategoryRepository;
import com.example.tesisaoskaunto.messageservice.domain.models.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category saveCategory(Long projectId, String categoryName) {
        Category category = new Category();
        category.setProjectId(projectId);
        category.setCategoryName(categoryName);
        Category categoryToSave = categoryRepository.save(category);
        return categoryToSave;
    }

    public List<Category> getCategoriesByProjectId(Long projectId) {
        return categoryRepository
                .findByProjectId(projectId);
    }
}