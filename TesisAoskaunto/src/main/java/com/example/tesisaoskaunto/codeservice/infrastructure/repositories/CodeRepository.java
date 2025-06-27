package com.example.tesisaoskaunto.codeservice.infrastructure.repositories;

import com.example.tesisaoskaunto.codeservice.domain.models.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {
    Optional<Code> findByProjectId(Long projectId);

    List<Code> findAllByProjectId(Long projectId);
}