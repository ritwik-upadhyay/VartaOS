package com.vartaos.vartaosbackend.ai.prompt;

import com.vartaos.vartaosbackend.ai.context.AIContext;

public interface PromptBuilder {

    String build(AIContext context);

}