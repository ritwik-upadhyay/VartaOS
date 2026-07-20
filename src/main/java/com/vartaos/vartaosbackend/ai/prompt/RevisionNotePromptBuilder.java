package com.vartaos.vartaosbackend.ai.prompt;

import com.vartaos.vartaosbackend.ai.context.AIContext;
import com.vartaos.vartaosbackend.ai.note.NoteGenerationContext;
import com.vartaos.vartaosbackend.ai.note.NotePromptBuilder;
import org.springframework.stereotype.Component;

@Component
public class RevisionNotePromptBuilder implements NotePromptBuilder {

    @Override
    public String build(NoteGenerationContext context) {

        return """
                You are the AI tutor for VartaOS.

                Generate concise Revision Notes for the following topic.

                The notes should:
                - Be optimized for quick revision.
                - Use headings and bullet points.
                - Include only the most important concepts.
                - Mention formulas, rules, or syntax if applicable.
                - Include memory tricks where useful.
                - Keep the notes short and high-value.
                - Do NOT explain concepts in detail.
                - Return only the revision note in Markdown format.

                Topic:
                %s
                """.formatted(context.topic());
    }
}