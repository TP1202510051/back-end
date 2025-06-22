package com.example.tesisaoskaunto.productservice.infrastructure.repository;

import com.example.tesisaoskaunto.productservice.domain.models.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Size, Long> {
}
