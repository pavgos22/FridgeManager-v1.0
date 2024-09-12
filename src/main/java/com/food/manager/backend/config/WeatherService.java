package com.food.manager.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class WeatherService {
    @Value("${weather.api-key}")
    private String weatherKey;
}
