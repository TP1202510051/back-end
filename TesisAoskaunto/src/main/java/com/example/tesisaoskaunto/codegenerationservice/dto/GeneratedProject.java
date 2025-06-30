// src/main/java/com/example/tesisaoskaunto/codegenerationservice/dto/GeneratedProject.java
package com.example.tesisaoskaunto.codegenerationservice.dto;

public class GeneratedProject {

    private final String fileName;
    private final byte[] fileContent;

    public GeneratedProject(String fileName, byte[] fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFileContent() {
        return fileContent;
    }
}