package com.example.tesisaoskaunto.categoryservice.application;

import com.example.tesisaoskaunto.categoryservice.domain.dto.CategoryRequest;
import com.example.tesisaoskaunto.categoryservice.domain.dto.UpdateCategoryRequest;
import com.example.tesisaoskaunto.categoryservice.domain.models.Category;
import com.example.tesisaoskaunto.categoryservice.infrastructure.repositories.CategoryRepository;
import com.example.tesisaoskaunto.categoryservice.service.CategoryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final CategoryService categoryAssistant;

    public CategoryController(CategoryRepository categoryRepository, CategoryService categoryAssistant) {
        this.categoryRepository = categoryRepository;
        this.categoryAssistant = categoryAssistant;
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest request) {
        Category generatedResponse = categoryAssistant.saveCategory(request.getProjectId(), request.getName());
        return ResponseEntity.ok(generatedResponse);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Category>> getCategoryByProjectId(@PathVariable Long projectId) {
        List<Category> categories = categoryRepository.findByProjectId(projectId);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @RequestBody UpdateCategoryRequest request) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Category cat = category.get();
        cat.setCategoryName(request.getName());
        categoryRepository.save(cat);
        return ResponseEntity.ok("Category updated: " + cat.getCategoryName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWindow(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoryRepository.deleteById(id);
        return ResponseEntity.ok("Category deleted");
    }
}
