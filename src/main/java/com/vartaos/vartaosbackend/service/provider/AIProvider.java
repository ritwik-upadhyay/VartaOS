package com.vartaos.vartaosbackend.service.provider;

import com.vartaos.vartaosbackend.dto.ai.GeminiChatResponse;

public interface AIProvider {

    GeminiChatResponse chat(String prompt);

    String generateText(String prompt);
}