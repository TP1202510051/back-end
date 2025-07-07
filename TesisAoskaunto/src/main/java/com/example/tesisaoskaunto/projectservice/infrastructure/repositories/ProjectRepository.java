package com.example.tesisaoskaunto.projectservice.infrastructure.repositories;

import com.example.tesisaoskaunto.projectservice.domain.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUserId(String projectId);
}