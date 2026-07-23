package com.vartaos.vartaosbackend.dto.ai;

import com.vartaos.vartaosbackend.entity.enums.AIProviderType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAISettingsRequest {

    @NotNull(message = "AI provider is required.")
    private AIProviderType provider;

    private String model;
}
