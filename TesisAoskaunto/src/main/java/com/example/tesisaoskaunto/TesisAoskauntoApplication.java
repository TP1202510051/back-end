package com.example.tesisaoskaunto;

import io.github.cdimascio.dotenv.Dotenv;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import io.swagger.v3.oas.annotations.info.Info;

import java.io.File;
import java.io.FileWriter;


@SpringBootApplication
@EnableJpaAuditing
@OpenAPIDefinition(info = @Info(title = "Mi API", version = "v1", description = "Documentación de mi servicio"))
public class TesisAoskauntoApplication {


    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("PROJECT_ID", dotenv.get("PROJECT_ID"));
        System.setProperty("TOPIC-PROMPT", dotenv.get("TOPIC-PROMPT"));
        System.setProperty("SUBSCRIPTION_PROMPT", dotenv.get("SUBSCRIPTION_PROMPT"));
        System.setProperty("FUNCION_RECEIVER", dotenv.get("FUNCION_RECEIVER"));
        System.setProperty("IA_TOKEN", dotenv.get("IA_TOKEN"));
        System.setProperty("API_POST_URL", dotenv.get("API_POST_URL"));

        String credentialsJson = dotenv.get("GOOGLE_APPLICATION_CREDENTIALS");
        if (credentialsJson != null && !credentialsJson.isEmpty()) {
            try {
                File tempFile = File.createTempFile("gcp-creds", ".json");
                tempFile.deleteOnExit();
                try (FileWriter writer = new FileWriter(tempFile)) {
                    writer.write(credentialsJson);
                }
                System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", tempFile.getAbsolutePath());
            } catch (Exception e) {
                throw new RuntimeException("Error al crear archivo temporal de credenciales GCP", e);
            }
        }

        SpringApplication.run(TesisAoskauntoApplication.class, args);
    }
}
