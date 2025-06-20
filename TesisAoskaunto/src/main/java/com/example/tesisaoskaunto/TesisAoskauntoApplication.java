package com.example.tesisaoskaunto;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class TesisAoskauntoApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .directory("src") // Cambia esto si tu .env está en otra carpeta
                .ignoreIfMissing()
                .load();
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("PROJECT_ID", dotenv.get("PROJECT_ID"));
        System.setProperty("TOPIC-PROMPT", dotenv.get("TOPIC-PROMPT"));
        System.setProperty("TOPIC-RESPONSE", dotenv.get("TOPIC-RESPONSE"));
        System.setProperty("SUBSCRIPTION_PROMPT", dotenv.get("SUBSCRIPTION_PROMPT"));
        System.setProperty("SUBSCRIPTION_RESPONSE", dotenv.get("SUBSCRIPTION_RESPONSE"));
        System.setProperty("FUNCION_RECEIVER", dotenv.get("FUNCION_RECEIVER"));
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", dotenv.get("GOOGLE_APPLICATION_CREDENTIALS"));
        System.setProperty("IA_TOKEN", dotenv.get("IA_TOKEN"));
        System.setProperty("API_POST_URL", dotenv.get("API_POST_URL"));


        SpringApplication.run(TesisAoskauntoApplication.class, args);

    }
}
