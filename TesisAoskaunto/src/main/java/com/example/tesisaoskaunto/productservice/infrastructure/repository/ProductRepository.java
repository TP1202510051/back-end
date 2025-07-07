package com.example.tesisaoskaunto.productservice.infrastructure.repository;

import com.example.tesisaoskaunto.categoryservice.domain.models.Category;
import com.example.tesisaoskaunto.productservice.domain.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
}
