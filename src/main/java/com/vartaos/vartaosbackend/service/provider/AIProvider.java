package com.vartaos.vartaosbackend.service.provider;

import com.vartaos.vartaosbackend.entity.enums.AIProviderType;

import java.util.List;

public interface AIProvider {

    AIProviderType getType();

    String getDisplayName();

    String getDefaultModel();

    boolean isAvailable();

    String getUnavailableMessage();

    List<String> getAvailableModels();

    String generate(AITask task, String prompt, String model);
}
