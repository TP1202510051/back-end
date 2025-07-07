package com.example.tesisaoskaunto.assistantservice.utils;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class GcpCredentialsUtil {

    public static GoogleCredentials loadCredentialsFromEnv() throws Exception {
        String credentialsJson = System.getenv("GOOGLE_CREDENTIALS_JSON");
        if (credentialsJson == null || credentialsJson.isEmpty()) {
            throw new IllegalStateException("La variable de entorno GOOGLE_CREDENTIALS_JSON no está definida.");
        }

        try (InputStream credentialsStream = new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8))) {
            return GoogleCredentials.fromStream(credentialsStream);
        }
    }
}
