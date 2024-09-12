package com.food.manager.backend.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Configuration
public class OAuthService {

    @Value("${oauth.client-id}")
    private String clientId;

    @Value("${oauth.client-secret}")
    private String clientSecret;

    @Value("${oauth.token-url}")
    private String tokenUrl;

    private String oAuthToken;
    private long tokenExpiryTime;

    private final RestTemplate restTemplate;

    public OAuthService() {
        this.restTemplate = new RestTemplate();
    }

    public String getOAuthToken() {
        if (oAuthToken == null || isTokenExpired()) {
            refreshOAuthToken();
        }
        return oAuthToken;
    }

    private boolean isTokenExpired() {
        return Instant.now().getEpochSecond() >= tokenExpiryTime;
    }

    private void refreshOAuthToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        String body = "grant_type=client_credentials";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            parseTokenResponse(response.getBody());
        } else {
            throw new RuntimeException("Failed to refresh OAuth token");
        }
    }

    private void parseTokenResponse(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);
            this.oAuthToken = jsonNode.get("access_token").asText();
            long expiresIn = jsonNode.get("expires_in").asLong();
            this.tokenExpiryTime = Instant.now().getEpochSecond() + expiresIn;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse OAuth token response", e);
        }
    }
}
