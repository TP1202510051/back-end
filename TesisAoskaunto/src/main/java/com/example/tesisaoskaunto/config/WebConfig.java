package com.example.tesisaoskaunto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Permitir los métodos relevantes
                .allowedHeaders("*")  // Permitir cualquier encabezado
                .allowedOrigins("http://localhost:5173")  // Especificar el origen de tu frontend
                .allowCredentials(true);  // Permitir el uso de credenciales (si es necesario)
    }
}
