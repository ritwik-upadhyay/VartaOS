package com.vartaos.vartaosbackend.service.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.vartaos.vartaosbackend.entity.enums.AIProviderType;
import com.vartaos.vartaosbackend.exception.AIProviderException;
import com.vartaos.vartaosbackend.exception.AIProviderUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GeminiProvider implements AIProvider {

    private final Optional<Client> geminiClient;
    @Value("${gemini.model:gemini-2.5-flash}")
    private String defaultModel;

    @Override
    public AIProviderType getType() {
        return AIProviderType.GEMINI;
    }

    @Override
    public String getDisplayName() {
        return "Gemini";
    }

    @Override
    public String getDefaultModel() {
        return defaultModel;
    }

    @Override
    public boolean isAvailable() {
        return geminiClient.isPresent();
    }

    @Override
    public String getUnavailableMessage() {
        return "Gemini is not configured on the backend. Add a valid Gemini API key to enable it.";
    }

    @Override
    public List<String> getAvailableModels() {
        return List.of(defaultModel);
    }

    @Override
    public String generate(AITask task, String prompt, String model) {

        GenerateContentResponse response = generateContent(
                enrichPrompt(task, prompt),
                resolveModel(model)
        );
        return normalizeTextResponse(response.text());
    }

    private GenerateContentResponse generateContent(String prompt, String model) {

        Client client = geminiClient.orElseThrow(
                () -> new AIProviderUnavailableException(getUnavailableMessage())
        );

        try {
            return client.models.generateContent(
                    model,
                    prompt,
                    null
            );
        } catch (RuntimeException ex) {
            throw mapException(ex);
        }
    }

    private RuntimeException mapException(RuntimeException ex) {

        String message = ex.getMessage();
        String normalized = message == null
                ? ""
                : message.toLowerCase(Locale.ROOT);

        if (normalized.contains("quota")
                || normalized.contains("resource_exhausted")
                || normalized.contains("rate limit")) {
            return new AIProviderException(
                    "Gemini quota was exceeded. Please try again later or switch to Ollama in Settings."
            );
        }

        if (normalized.contains("api key")
                || normalized.contains("permission")
                || normalized.contains("unauthorized")
                || normalized.contains("forbidden")) {
            return new AIProviderUnavailableException(
                    "Gemini is currently unavailable for this account. Please check the backend Gemini configuration or switch to Ollama."
            );
        }

        return new AIProviderException(
                "Gemini could not complete this request right now. Please try again."
        );
    }

    private String resolveModel(String requestedModel) {
        return StringUtils.hasText(requestedModel)
                ? requestedModel.trim()
                : defaultModel;
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
                    Respond as a helpful study assistant.
                    Return only the final markdown answer for the user.

                    %s
                    """.formatted(prompt);
            case CHAT_TITLE -> """
                    Return only a short plain-text conversation title.

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
