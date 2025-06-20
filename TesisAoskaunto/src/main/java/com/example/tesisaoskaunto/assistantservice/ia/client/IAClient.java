package com.example.tesisaoskaunto.assistantservice.ia.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IAClient {

    private static final String API_URL = System.getProperty("API_POST_URL");
    private static final String API_KEY = System.getProperty("IA_TOKEN");;

    public String generateResponse(String prompt) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "mistralai/Mixtral-8x7B-Instruct-v0.1");
        body.put("prompt", prompt);
        body.put("max_tokens", 100);
        body.put("temperature", 0.7);
        body.put("stop", List.of("</s>"));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, entity, Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        return choices.get(0).get("text").toString().trim();
    }
}
