package com.food.manager.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;

@Service
public class OAuthService {

    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${CLIENT_SECRET}")
    private String clientSecret;

    @Value("${TOKEN_URL}")
    private String tokenUrl;

    @Getter
    @Value("${WEATHER_KEY}")
    private String weatherKey;

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
