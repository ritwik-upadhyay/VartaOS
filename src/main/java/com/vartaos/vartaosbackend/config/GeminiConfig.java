package com.vartaos.vartaosbackend.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the Gemini API client.
 *
 * @author Ritwik
 * @since VartaOS v1
 */
@Configuration
public class GeminiConfig {

    @Value("${gemini.api.key:}")
    private String apiKey;

    /**
     * Creates a singleton Gemini client.
     *
     * @return Configured Gemini client.
     */
    @Bean
    @ConditionalOnExpression(
            "T(org.springframework.util.StringUtils).hasText('${gemini.api.key:}')"
    )
    public Client geminiClient() {
        return Client.builder()
                .apiKey(apiKey)
                .build();
    }
}
