package com.vartaos.vartaosbackend.dto.ai;

import com.vartaos.vartaosbackend.entity.enums.AIProviderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AISettingsResponse {
    private AIProviderType provider;
    private String model;
    private List<AIProviderOptionResponse> providers;
}
