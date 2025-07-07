package com.example.tesisaoskaunto.categoryservice.infrastructure.repositories;

import com.example.tesisaoskaunto.categoryservice.domain.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByProjectId(Long projectId);
}
