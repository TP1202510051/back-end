package com.example.tesisaoskaunto.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI MathPlayOpenPlatformOpenApi() {
        // General configuration
        var openApi = new OpenAPI();
        openApi
                .info(new Info()
                        .title("Tesis Aoskaunto API")
                        .description("Tesis Aoskaunto Platform application REST API documentation.")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0")
                                .url("https://springdoc.org")));
        // Add security scheme

        return openApi;
    }
}
