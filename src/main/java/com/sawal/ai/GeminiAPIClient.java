package com.sawal.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GeminiAPIClient {

    // Inject the API key from application.yml
    @Value("${gemini.api.key}")
    private String apiKey;
    
    // RestTemplate for making HTTP calls
    private final RestTemplate restTemplate = new RestTemplate();
    
    /**
     * Sends a natural language query to the Gemini API via Vertex AI.
     *
     * The endpoint used is from the quickstart guide:
     * https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=YOUR_API_KEY
     *
     * @param naturalLanguageQuery the query in plain language
     * @return the raw API response as a string
     */
    public String sendQuery(String naturalLanguageQuery) {
        // Construct the URL with the API key as a query parameter
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;
        
        // Set up HTTP headers; Content-Type is required
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        
        // Construct the request body in accordance with the quickstart guide.
        // Here we wrap the natural language query in the "contents" field.
        String requestBody = "{ \"contents\": [ { \"parts\": [ { \"text\": \"" + naturalLanguageQuery + "\" } ] } ] }";
        
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        
        // Make the POST request
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response.getBody();
    }
    
    /**
     * Parses the API response.
     *
     * Note: This is a stub implementation. In a real scenario, you would
     * parse the JSON response to extract the SQL query or any other relevant
     * information.
     *
     * @param response the raw API response as a string
     * @return the processed SQL query (or response)
     */
    public String parseResponse(String response) {
        // For demonstration purposes, we return the raw response.
        // Implement your parsing logic here.
        return response;
    }
}
