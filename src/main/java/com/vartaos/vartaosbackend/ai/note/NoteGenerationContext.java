package com.vartaos.vartaosbackend.ai.note;

/**
 * Context required for AI note generation.
 */
public record NoteGenerationContext(
        String topic
) {
}