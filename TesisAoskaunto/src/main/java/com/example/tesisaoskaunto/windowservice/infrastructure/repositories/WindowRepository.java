package com.example.tesisaoskaunto.windowservice.infrastructure.repositories;

import com.example.tesisaoskaunto.windowservice.domain.models.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WindowRepository extends JpaRepository<Window, Long> {
    List<Window> findByProjectId(Long projectId);
}