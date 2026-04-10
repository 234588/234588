package com.example.aiproject.service;

import com.example.aiproject.config.AiProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@Service
public class AiChatService {

    private final AiProperties properties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public AiChatService(AiProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String chat(String userMessage) {
        if (userMessage == null || userMessage.isBlank()) {
            return "请输入有效问题。";
        }

        String apiKey = properties.apiKey();
        if (apiKey == null || apiKey.isBlank()) {
            return "缺少 ai.api-key 配置，请先在环境变量或配置文件中设置。";
        }

        String baseUrl = properties.baseUrl() == null || properties.baseUrl().isBlank()
                ? "https://api.openai.com/v1"
                : properties.baseUrl();
        String model = properties.model() == null || properties.model().isBlank()
                ? "gpt-4o-mini"
                : properties.model();

        try {
            String body = objectMapper.writeValueAsString(Map.of(
                    "model", model,
                    "messages", new Object[]{
                            Map.of("role", "system", "content", "你是一个乐于助人的 Java AI 助手。"),
                            Map.of("role", "user", "content", userMessage)
                    }
            ));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .timeout(Duration.ofSeconds(30))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                return "AI 接口调用失败，状态码: " + response.statusCode() + "，响应: " + response.body();
            }

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode contentNode = root.path("choices").path(0).path("message").path("content");
            if (contentNode.isMissingNode() || contentNode.isNull()) {
                return "AI 返回结果中没有可用内容。";
            }

            return contentNode.asText();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "调用 AI 被中断: " + e.getMessage();
        } catch (IOException e) {
            return "调用 AI 失败: " + e.getMessage();
        }
    }
}
