package com.vartaos.vartaosbackend.service.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.vartaos.vartaosbackend.dto.ai.GeminiChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeminiProvider implements AIProvider {

    private final Client geminiClient;
    private final ObjectMapper objectMapper;

    @Override
    public GeminiChatResponse chat(String prompt) {

        GenerateContentResponse response =
                geminiClient.models.generateContent(
                        "gemini-2.5-flash",
                        prompt,
                        null
                );

        String rawResponse = response.text();

        try {
            return objectMapper.readValue(
                    rawResponse,
                    GeminiChatResponse.class
            );

        } catch (JsonProcessingException e) {

            throw new RuntimeException(
                    "Failed to parse Gemini response.",
                    e
            );

        }
    }

    @Override
    public String generateText(String prompt) {

        GenerateContentResponse response =
                geminiClient.models.generateContent(
                        "gemini-2.5-flash",
                        prompt,
                        null
                );

        return response.text();
    }
}