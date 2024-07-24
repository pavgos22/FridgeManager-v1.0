package com.food.manager.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

    @Value("${P_TOKEN}")
    private String oAuthToken;

    @Value("${WEATHER_KEY}")
    private String weatherKey;

    public String getOAuthToken() {
        return oAuthToken;
    }

    public String getWeatherKey() {
        return weatherKey;
    }
}
