package com.example.tesisaoskaunto.productservice.infrastructure.repository;

import com.example.tesisaoskaunto.productservice.domain.models.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface SizeRepository extends JpaRepository<Size, Long> {
    Set<Size> findByIdIn(Set<Long> sizeIds);

    Optional<Size> findByName(String name);
}