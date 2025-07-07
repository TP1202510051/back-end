// src/main/java/com/example/tesisaoskaunto/codegenerationservice/service/ZipService.java
package com.example.tesisaoskaunto.codegenerationservice.service;

import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.stream.Stream;

@Service
public class ZipService {
    public byte[] createZip(Path sourceDir) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            try (Stream<Path> paths = Files.walk(sourceDir)) {
                paths
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceDir.relativize(path).toString());
                        try {
                            zipOutputStream.putNextEntry(zipEntry);
                            Files.copy(path, zipOutputStream);
                            zipOutputStream.closeEntry();
                        } catch (IOException e) {
                            System.err.println("Error al añadir al zip: " + path);
                        }
                    });
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
}