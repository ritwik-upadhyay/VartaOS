package com.vartaos.vartaosbackend.ai.note;

public interface NotePromptBuilder {

    String build(NoteGenerationContext context);

}