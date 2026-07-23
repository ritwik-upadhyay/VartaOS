package com.vartaos.vartaosbackend.service.provider;

import com.vartaos.vartaosbackend.entity.User;
import com.vartaos.vartaosbackend.entity.enums.AIProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AIProviderRegistry {

    private final List<AIProvider> providerImplementations;

    public AIProvider getProvider(AIProviderType type) {
        AIProvider provider = providers().get(type);
        if (provider == null) {
            throw new RuntimeException("AI provider is not registered: " + type);
        }
        return provider;
    }

    public AIProvider resolveProvider(User user) {
        AIProviderType type = user.getPreferredAiProvider() == null
                ? AIProviderType.GEMINI
                : user.getPreferredAiProvider();

        return getProvider(type);
    }

    public String resolveModel(User user, AIProvider provider) {
        if (StringUtils.hasText(user.getPreferredAiModel())) {
            return user.getPreferredAiModel().trim();
        }
        return provider.getDefaultModel();
    }

    public List<AIProvider> getProviders() {
        return providerImplementations;
    }

    private Map<AIProviderType, AIProvider> providers() {
        Map<AIProviderType, AIProvider> providers = new EnumMap<>(AIProviderType.class);
        for (AIProvider provider : providerImplementations) {
            providers.put(provider.getType(), provider);
        }
        return providers;
    }
}
