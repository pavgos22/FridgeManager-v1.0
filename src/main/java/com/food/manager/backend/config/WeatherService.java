package com.food.manager.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class WeatherService {
    @Value("${WEATHER_KEY}")
    private String weatherKey;
}
