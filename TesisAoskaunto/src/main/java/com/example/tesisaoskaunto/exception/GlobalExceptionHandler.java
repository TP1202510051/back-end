// src/main/java/com/example/tesisaoskaunto/exception/GlobalExceptionHandler.java
package com.example.tesisaoskaunto.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
        // --- ESTA ES LA PARTE MÁS IMPORTANTE ---
        // Imprime la traza completa del error en la consola del servidor.
        System.err.println("!!! EXCEPCIÓN GLOBAL CAPTURADA !!!");
        ex.printStackTrace();
        // -----------------------------------------

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());

        // También incluimos la traza en la respuesta JSON para depuración
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        body.put("trace", sw.toString());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}