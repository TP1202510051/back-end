package com.example.tesisaoskaunto.productservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SizeDto {
    private String name;

    // anota el campo para que Jackson sepa que aquí va "isActive"
    @JsonProperty("isActive")
    private boolean isActive;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // getter correcto: isActive() → propiedad "isActive"
    public boolean isActive() {
        return isActive;
    }
    // ahora el setter coincide con la propiedad JSON "isActive"
    @JsonProperty("isActive")
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
