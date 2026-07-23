package com.vartaos.vartaosbackend.service.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vartaos.vartaosbackend.entity.enums.AIProviderType;
import com.vartaos.vartaosbackend.exception.AIProviderException;
import com.vartaos.vartaosbackend.exception.AIProviderUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OllamaProvider implements AIProvider {

    private static final Duration REQUEST_TIMEOUT = Duration.ofMinutes(5);

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    @Value("${ollama.base-url:http://localhost:11434}")
    private String baseUrl;

    @Value("${ollama.model:}")
    private String defaultModel;

    @Override
    public AIProviderType getType() {
        return AIProviderType.OLLAMA;
    }

    @Override
    public String getDisplayName() {
        return "Ollama";
    }

    @Override
    public String getDefaultModel() {
        if (StringUtils.hasText(defaultModel)) {
            return defaultModel.trim();
        }

        List<String> models = getAvailableModels();
        if (!models.isEmpty()) {
            return models.get(0);
        }

        return "";
    }

    @Override
    public boolean isAvailable() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(normalizeBaseUrl() + "/api/tags"))
                    .timeout(Duration.ofSeconds(3))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            return response.statusCode() >= 200 && response.statusCode() < 300;
        } catch (IOException | InterruptedException ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return false;
        }
    }

    @Override
    public String getUnavailableMessage() {
        return "Ollama is not running on http://localhost:11434. Start Ollama and try again.";
    }

    @Override
    public List<String> getAvailableModels() {

        try {
            JsonNode root = sendJsonRequest(
                    "/api/tags",
                    "GET",
                    null,
                    Duration.ofSeconds(5)
            );

            List<String> models = new ArrayList<>();
            for (JsonNode modelNode : root.path("models")) {
                String modelName = modelNode.path("name").asText();
                if (StringUtils.hasText(modelName)) {
                    models.add(modelName.trim());
                }
            }
            return models;
        } catch (AIProviderUnavailableException ex) {
            return List.of();
        }
    }

    @Override
    public String generate(AITask task, String prompt, String model) {

        String resolvedModel = resolveModel(model);

        if (!StringUtils.hasText(resolvedModel)) {
            throw new AIProviderUnavailableException(
                    "No Ollama model is installed yet. Install a model in Ollama, then try again."
            );
        }

        try {
            String requestBody = objectMapper.writeValueAsString(
                    java.util.Map.of(
                            "model", resolvedModel,
                            "prompt", enrichPrompt(task, prompt),
                            "stream", false
                    )
            );

            JsonNode root = sendJsonRequest(
                    "/api/generate",
                    "POST",
                    requestBody,
                    REQUEST_TIMEOUT
            );

            String response = root.path("response").asText();
            if (!StringUtils.hasText(response)) {
                throw new AIProviderException(
                        "Ollama returned an empty response. Please try again."
                );
            }

            return normalizeTextResponse(response);
        } catch (JsonProcessingException ex) {
            throw new AIProviderException(
                    "Ollama request could not be prepared. Please try again."
            );
        }
    }

    private JsonNode sendJsonRequest(
            String path,
            String method,
            String body,
            Duration timeout
    ) {

        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(normalizeBaseUrl() + path))
                    .timeout(timeout)
                    .header("Content-Type", "application/json");

            HttpRequest request = "POST".equals(method)
                    ? requestBuilder.POST(
                    HttpRequest.BodyPublishers.ofString(body == null ? "" : body)
            ).build()
                    : requestBuilder.GET().build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new AIProviderException(
                        "Ollama request failed. Please verify the selected model and try again."
                );
            }

            return objectMapper.readTree(response.body());
        } catch (IOException ex) {
            throw new AIProviderUnavailableException(getUnavailableMessage());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new AIProviderException(
                    "Ollama request was interrupted. Please try again."
            );
        }
    }

    private String resolveModel(String requestedModel) {
        if (StringUtils.hasText(requestedModel)) {
            return requestedModel.trim();
        }
        return getDefaultModel();
    }

    private String normalizeBaseUrl() {
        return baseUrl.endsWith("/")
                ? baseUrl.substring(0, baseUrl.length() - 1)
                : baseUrl;
    }

    private String normalizeTextResponse(String rawResponse) {

        if (rawResponse == null) {
            return "";
        }

        String normalized = rawResponse.trim();

        if (normalized.startsWith("```")) {
            normalized = normalized
                    .replaceFirst("^```json\\s*", "")
                    .replaceFirst("^```\\s*", "")
                    .replaceFirst("\\s*```$", "")
                    .trim();
        }

        return normalized;
    }

    private String enrichPrompt(AITask task, String prompt) {
        return switch (task) {
            case CHAT -> """
                    You are VartaOS AI, a calm and knowledgeable computer science study assistant.
                    Return only the final answer for the student in natural Markdown.
                    Do not return JSON, metadata, or field names.

                    %s
                    """.formatted(prompt);
            case CHAT_TITLE -> """
                    Return only a short plain-text conversation title.
                    Do not return JSON, bullets, or markdown.

                    %s
                    """.formatted(prompt);
            case GENERATE_LEARNING_NOTE,
                 GENERATE_REVISION_NOTE,
                 GENERATE_INTERVIEW_QUESTIONS,
                 GENERATE_MISTAKES,
                 GENERATE_RESOURCES -> prompt;
        };
    }
}
