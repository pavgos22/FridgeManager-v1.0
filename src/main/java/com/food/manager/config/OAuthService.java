package com.food.manager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

    @Value("${OAUTH_NEW_TOKEN}")
    private String oAuthToken;

    public String getOAuthToken() {
        return oAuthToken;
    }
}
