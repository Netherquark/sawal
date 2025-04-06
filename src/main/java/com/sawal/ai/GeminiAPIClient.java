package com.sawal.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

@Component
public class GeminiAPIClient {

    private static final Logger logger = LoggerFactory.getLogger(GeminiAPIClient.class);
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.ai.vertex.ai.gemini.credentials.location}")
    private String credentialsPath;

    public String sendQuery(String prompt) {
        try {
            String accessToken = getAccessToken();
            String requestBody = buildRequestBody(prompt);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return parseResponse(response.getBody());
            } else {
                logger.error("Gemini API responded with status: {}", response.getStatusCode());
                return "Error: Gemini API request failed.";
            }
        } catch (Exception e) {
            logger.error("Failed to query Gemini", e);
            return "Error: Gemini query failed.";
        }
    }

    private String buildRequestBody(String prompt) {
        return String.format("""
            {
              "contents": [
                {
                  "parts": [
                    {
                      "text": "%s"
                    }
                  ]
                }
              ]
            }
            """, prompt.replace("\"", "\\\""));
    }

    private String getAccessToken() throws IOException {
        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream)
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/generative-language"));
            credentials.refreshIfExpired();
            return credentials.getAccessToken().getTokenValue();
        }
    }

    private String parseResponse(String responseJson) throws IOException {
        JsonNode root = objectMapper.readTree(responseJson);
        JsonNode candidates = root.path("candidates");

        if (candidates.isArray() && candidates.size() > 0) {
            JsonNode textNode = candidates.get(0).path("content").path("parts").get(0).path("text");
            if (textNode.isTextual()) {
                return textNode.asText();
            }
        }

        logger.warn("Could not parse Gemini response: {}", responseJson);
        return "No valid content found in response.";
    }
}
