package com.vartaos.vartaosbackend.service;

import com.vartaos.vartaosbackend.dto.ai.AIProviderOptionResponse;
import com.vartaos.vartaosbackend.dto.ai.AISettingsResponse;
import com.vartaos.vartaosbackend.dto.ai.UpdateAISettingsRequest;
import com.vartaos.vartaosbackend.entity.User;
import com.vartaos.vartaosbackend.entity.enums.AIProviderType;
import com.vartaos.vartaosbackend.exception.AIProviderUnavailableException;
import com.vartaos.vartaosbackend.repository.UserRepository;
import com.vartaos.vartaosbackend.service.provider.AIProvider;
import com.vartaos.vartaosbackend.service.provider.AIProviderRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AISettingsService {

    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final AIProviderRegistry aiProviderRegistry;

    @Transactional(readOnly = true)
    public AISettingsResponse getSettings() {
        User user = currentUserService.getCurrentUser();
        return buildResponse(user);
    }

    public AISettingsResponse updateSettings(UpdateAISettingsRequest request) {

        User user = currentUserService.getCurrentUser();
        AIProvider provider = aiProviderRegistry.getProvider(request.getProvider());

        if (!provider.isAvailable()) {
            throw new AIProviderUnavailableException(provider.getUnavailableMessage());
        }

        String model = resolveModel(provider, request.getModel());

        user.setPreferredAiProvider(request.getProvider());
        user.setPreferredAiModel(model);

        userRepository.save(user);

        return buildResponse(user);
    }

    @Transactional(readOnly = true)
    public List<AIProviderOptionResponse> getProviderOptions() {
        return aiProviderRegistry.getProviders()
                .stream()
                .map(this::mapProvider)
                .toList();
    }

    private AISettingsResponse buildResponse(User user) {

        AIProviderType providerType = user.getPreferredAiProvider() == null
                ? AIProviderType.GEMINI
                : user.getPreferredAiProvider();

        AIProvider provider = aiProviderRegistry.getProvider(providerType);
        String model = StringUtils.hasText(user.getPreferredAiModel())
                ? user.getPreferredAiModel().trim()
                : provider.getDefaultModel();

        return AISettingsResponse.builder()
                .provider(providerType)
                .model(model)
                .providers(getProviderOptions())
                .build();
    }

    private AIProviderOptionResponse mapProvider(AIProvider provider) {
        return AIProviderOptionResponse.builder()
                .type(provider.getType())
                .label(provider.getDisplayName())
                .available(provider.isAvailable())
                .message(provider.isAvailable()
                        ? "Ready"
                        : provider.getUnavailableMessage())
                .models(provider.getAvailableModels())
                .build();
    }

    private String resolveModel(AIProvider provider, String requestedModel) {

        List<String> availableModels = provider.getAvailableModels();

        if (!StringUtils.hasText(requestedModel)) {
            if (!availableModels.isEmpty()) {
                return availableModels.get(0);
            }
            return provider.getDefaultModel();
        }

        String model = requestedModel.trim();

        if (!availableModels.isEmpty() && !availableModels.contains(model)) {
            throw new RuntimeException("Selected AI model is not installed for " + provider.getDisplayName() + ".");
        }

        return model;
    }
}
