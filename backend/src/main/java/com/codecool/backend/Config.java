package com.codecool.backend;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("${google_maps.api_key}")
    private String API_KEY;

    @Bean
    public GeoApiContext context() {
        return new GeoApiContext.Builder().apiKey(API_KEY).build();
    }
}
