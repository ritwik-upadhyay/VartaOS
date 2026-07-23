package com.vartaos.vartaosbackend.service.impl;

import com.vartaos.vartaosbackend.ai.note.NoteGenerationContext;
import com.vartaos.vartaosbackend.ai.prompt.InterviewQuestionPromptBuilder;
import com.vartaos.vartaosbackend.ai.prompt.LearningNotePromptBuilder;
import com.vartaos.vartaosbackend.ai.prompt.MistakesLearningPromptBuilder;
import com.vartaos.vartaosbackend.ai.prompt.ResourcesPromptBuilder;
import com.vartaos.vartaosbackend.ai.prompt.RevisionNotePromptBuilder;
import com.vartaos.vartaosbackend.entity.Folder;
import com.vartaos.vartaosbackend.entity.Note;
import com.vartaos.vartaosbackend.entity.User;
import com.vartaos.vartaosbackend.entity.enums.NoteType;
import com.vartaos.vartaosbackend.repository.FolderRepository;
import com.vartaos.vartaosbackend.repository.NoteRepository;
import com.vartaos.vartaosbackend.service.ProgressService;
import com.vartaos.vartaosbackend.service.AINoteGenerationService;
import com.vartaos.vartaosbackend.service.CurrentUserService;
import com.vartaos.vartaosbackend.service.provider.AIProvider;
import com.vartaos.vartaosbackend.service.provider.AIProviderRegistry;
import com.vartaos.vartaosbackend.service.provider.AITask;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AINoteGenerationServiceImpl
        implements AINoteGenerationService {

    private final FolderRepository folderRepository;

    private final NoteRepository noteRepository;

    private final AIProviderRegistry aiProviderRegistry;

    private final LearningNotePromptBuilder learningPromptBuilder;

    private final RevisionNotePromptBuilder revisionPromptBuilder;

    private final InterviewQuestionPromptBuilder interviewPromptBuilder;

    private final MistakesLearningPromptBuilder mistakesPromptBuilder;

    private final ResourcesPromptBuilder resourcesPromptBuilder;

    private final ProgressService progressService;

    private final CurrentUserService currentUserService;

    private Note upsertTypedNote(
            Folder folder,
            String title,
            String content,
            NoteType type
    ) {

        Optional<Note> existingNote = noteRepository.findByFolderAndType(folder, type);

        Note note = existingNote.orElseGet(() -> Note.builder()
                .folder(folder)
                .type(type)
                .displayOrder(0)
                .build());

        note.setTitle(title);
        note.setContent(content);

        return note;
    }

    private Note upsertTitledNote(
            Folder folder,
            String title,
            String content,
            NoteType type
    ) {

        Note note = noteRepository.findByFolderAndTitleIgnoreCase(folder, title)
                .orElseGet(() -> Note.builder()
                        .folder(folder)
                        .type(type)
                        .displayOrder(0)
                        .build());

        note.setTitle(title);
        note.setContent(content);
        note.setType(type);

        return note;
    }


    @Override
    public void generateNotes(Long folderId) {

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        NoteGenerationContext context =
                new NoteGenerationContext(folder.getName());

        String learningPrompt = learningPromptBuilder.build(context);

        String revisionPrompt = revisionPromptBuilder.build(context);

        String interviewPrompt = interviewPromptBuilder.build(context);

        String mistakesPrompt = mistakesPromptBuilder.build(context);

        String resourcesPrompt = resourcesPromptBuilder.build(context);

        User user = currentUserService.getCurrentUser();
        AIProvider aiProvider = aiProviderRegistry.resolveProvider(user);
        String model = aiProviderRegistry.resolveModel(user, aiProvider);

        String learningContent = aiProvider.generate(
                AITask.GENERATE_LEARNING_NOTE,
                learningPrompt,
                model
        );

        String revisionContent = aiProvider.generate(
                AITask.GENERATE_REVISION_NOTE,
                revisionPrompt,
                model
        );

        String interviewContent = aiProvider.generate(
                AITask.GENERATE_INTERVIEW_QUESTIONS,
                interviewPrompt,
                model
        );

        String mistakesContent = aiProvider.generate(
                AITask.GENERATE_MISTAKES,
                mistakesPrompt,
                model
        );

        String resourcesContent = aiProvider.generate(
                AITask.GENERATE_RESOURCES,
                resourcesPrompt,
                model
        );

        List<Note> notesToSave = new ArrayList<>();

        Note learningNote = upsertTypedNote(
                folder,
                "Learning Note",
                learningContent,
                NoteType.LEARNING
        );

        Note revisionNote = upsertTypedNote(
                folder,
                "Revision Note",
                revisionContent,
                NoteType.REVISION
        );

        Note interviewNote = upsertTypedNote(
                folder,
                "Interview Questions",
                interviewContent,
                NoteType.INTERVIEW
        );

        Note mistakesNote = upsertTypedNote(
                folder,
                "Mistakes & Learnings",
                mistakesContent,
                NoteType.MISTAKES
        );

        Note resourcesNote = upsertTitledNote(
                folder,
                "Resources",
                resourcesContent,
                NoteType.GENERAL
        );

        notesToSave.add(learningNote);
        notesToSave.add(revisionNote);
        notesToSave.add(interviewNote);
        notesToSave.add(mistakesNote);
        notesToSave.add(resourcesNote);

        noteRepository.saveAll(notesToSave);
    }

    @Override
    public void markFolderCompleted(Long folderId) {

        progressService.markTopicComplete(folderId);
    }

}
