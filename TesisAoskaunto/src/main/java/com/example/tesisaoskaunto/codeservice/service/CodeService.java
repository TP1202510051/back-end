package com.example.tesisaoskaunto.codeservice.service;

import com.example.tesisaoskaunto.codeservice.domain.models.Code;
import com.example.tesisaoskaunto.codeservice.infrastructure.repositories.CodeRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeService {
    private final CodeRepository codeRepository;

    public CodeService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    public String saveCode(Long projectId, String code, Long messageId) {
        Code cod = new Code();
        cod.setProjectId(projectId);
        cod.setCode(code);
        cod.setMessageId(messageId);
        var conversationToSave = codeRepository.save(cod);
        return "La conversacion " + conversationToSave.getCode() + " fue recibida para el projecto " + conversationToSave.getProjectId();
    }

    public List<Code> getCodesByProjectId(Long projectId) {
        return codeRepository.findAllByProjectId(projectId);
    }
}
