package com.example.aiproject.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ai")
public record AiProperties(String apiKey, String baseUrl, String model) {
}
