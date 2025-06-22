package com.example.tesisaoskaunto.productservice.infrastructure.repository;

import com.example.tesisaoskaunto.productservice.domain.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
