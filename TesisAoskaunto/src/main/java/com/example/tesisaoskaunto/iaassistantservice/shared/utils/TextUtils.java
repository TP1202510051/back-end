package com.example.tesisaoskaunto.iaassistantservice.shared.utils;

public class TextUtils {

    public static boolean containsKeyword(String text, String keyword) {
        return text.toLowerCase().contains(keyword.toLowerCase());
    }
}
