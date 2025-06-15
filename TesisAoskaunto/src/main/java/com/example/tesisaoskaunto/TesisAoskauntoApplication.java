package com.example.tesisaoskaunto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class TesisAoskauntoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TesisAoskauntoApplication.class, args);
    }

}
