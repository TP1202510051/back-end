package com.example.tesisaoskaunto.codeservice.infrastructure.repositories;

import com.example.tesisaoskaunto.codeservice.domain.models.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {
    Optional<Code> findByWindowId(Long windowId);

    List<Code> findAllByWindowId(Long windowId);

    List<Code> findByWindowIdIn(List<Long> windowIds);

    Code findTopByWindowIdOrderByCreatedAtDesc(Long windowId);
}