package com.vartaos.vartaosbackend.service;

import com.vartaos.vartaosbackend.dto.note.NoteSummaryResponse;
import com.vartaos.vartaosbackend.entity.Note;
import com.vartaos.vartaosbackend.entity.User;
import com.vartaos.vartaosbackend.repository.NoteRepository;
import com.vartaos.vartaosbackend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.vartaos.vartaosbackend.dto.tag.CreateTagRequest;
import com.vartaos.vartaosbackend.dto.tag.TagResponse;
import com.vartaos.vartaosbackend.dto.tag.UpdateTagRequest;
import com.vartaos.vartaosbackend.entity.Tag;
import com.vartaos.vartaosbackend.entity.Workspace;
import com.vartaos.vartaosbackend.repository.TagRepository;
import com.vartaos.vartaosbackend.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for managing tags.
 */
@Service
@RequiredArgsConstructor
public class TagService {

    /**
     * Repository for tag operations.
     */
    private final TagRepository tagRepository;

    /**
     * Repository for workspace operations.
     */
    private final WorkspaceRepository workspaceRepository;

    /**
     * Repository for user operations.
     */
    private final UserRepository userRepository;

    /**
     * Repository for note operations.
     */
    private final NoteRepository noteRepository;

    /**
     * Returns the workspace belonging to
     * the currently authenticated user.
     *
     * @return Current user's workspace.
     */
    private Workspace getCurrentWorkspace() {

        // Get authenticated user's email
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        // Find authenticated user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        // Return user's workspace
        return workspaceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Workspace not found."));
    }

    /**
     * Creates a new tag inside the current workspace.
     *
     * @param request Tag creation request.
     * @return Created tag.
     */
    public TagResponse createTag(CreateTagRequest request) {

        // Get current workspace
        Workspace workspace = getCurrentWorkspace();

        // Check for duplicate tag name
        if (tagRepository.findByNameAndWorkspace(request.getName(), workspace).isPresent()) {
            throw new RuntimeException("Tag already exists.");
        }

        // Create tag
        Tag tag = Tag.builder()
                .name(request.getName())
                .workspace(workspace)
                .build();

        // Save tag
        tag = tagRepository.save(tag);

        // Return response
        return mapTag(tag);
    }

    /**
     * Converts a Tag entity into a TagResponse DTO.
     *
     * @param tag Tag entity.
     * @return Tag response.
     */
    private TagResponse mapTag(Tag tag) {

        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    /**
     * Returns all tags belonging to the
     * current workspace.
     *
     * @return List of tags.
     */
    public List<TagResponse> getTags() {

        // Get current workspace
        Workspace workspace = getCurrentWorkspace();

        // Get all tags
        return tagRepository.findByWorkspace(workspace)
                .stream()
                .map(this::mapTag)
                .toList();
    }

    /**
     * Updates an existing tag.
     *
     * @param id      Tag ID.
     * @param request Updated tag details.
     * @return Updated tag.
     */
    public TagResponse updateTag(Long id, UpdateTagRequest request) {

        // Get current workspace
        Workspace workspace = getCurrentWorkspace();

        // Find tag
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found."));

        // Ensure tag belongs to current workspace
        if (!tag.getWorkspace().getId().equals(workspace.getId())) {
            throw new RuntimeException("Access denied.");
        }

        // Check if another tag with the same name already exists
        tagRepository.findByNameAndWorkspace(request.getName(), workspace)
                .ifPresent(existingTag -> {
                    if (!existingTag.getId().equals(tag.getId())) {
                        throw new RuntimeException("Tag already exists.");
                    }
                });

        // Update tag
        tag.setName(request.getName());

        // Save
        tagRepository.save(tag);

        // Return response
        return mapTag(tag);
    }

    /**
     * Deletes an existing tag.
     *
     * @param id Tag ID.
     */
    public void deleteTag(Long id) {

        // Get current workspace
        Workspace workspace = getCurrentWorkspace();

        // Find tag
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found."));

        // Ensure tag belongs to current workspace
        if (!tag.getWorkspace().getId().equals(workspace.getId())) {
            throw new RuntimeException("Access denied.");
        }

        // Delete tag
        tagRepository.delete(tag);
    }

    /**
     * Assigns a tag to a note.
     *
     * @param noteId Note ID.
     * @param tagId  Tag ID.
     */
    public void assignTagToNote(Long noteId, Long tagId) {

        // Get current workspace
        Workspace workspace = getCurrentWorkspace();

        // Find note
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found."));

        // Find tag
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found."));

        // Ensure note belongs to current workspace
        if (!note.getFolder()
                .getWorkspace()
                .getId()
                .equals(workspace.getId())) {

            throw new RuntimeException("Access denied.");
        }

        // Ensure tag belongs to current workspace
        if (!tag.getWorkspace().getId().equals(workspace.getId())) {
            throw new RuntimeException("Access denied.");
        }

        // Assign tag if not already assigned
        if (!note.getTags().contains(tag)) {
            note.getTags().add(tag);
        }

        // Save note
        noteRepository.save(note);
    }

    public void removeTagFromNote(Long noteId, Long tagId) {

        // Get current workspace
        Workspace workspace = getCurrentWorkspace();

        // Find note
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found."));

        // Find tag
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found."));

        // Ensure note belongs to current workspace
        if (!note.getFolder()
                .getWorkspace()
                .getId()
                .equals(workspace.getId())) {

            throw new RuntimeException("Access denied.");
        }

        // Ensure tag belongs to current workspace
        if (!tag.getWorkspace().getId().equals(workspace.getId())) {
            throw new RuntimeException("Access denied.");
        }

        // Remove tag from note
        note.getTags().remove(tag);

        // Save note
        noteRepository.save(note);

    }

    /**
     * Retrieves all tags assigned to a specific note.
     *
     * @param noteId ID of the note whose tags are to be fetched.
     * @return List of TagResponse DTOs representing all tags assigned to the note.
     */
    public List<TagResponse> getTagsOfNote(Long noteId) {

        // Get the workspace of the currently authenticated user
        Workspace workspace = getCurrentWorkspace();

        // Find the requested note
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found."));

        // Ensure the note belongs to the current user's workspace
        if (!note.getFolder()
                .getWorkspace()
                .getId()
                .equals(workspace.getId())) {

            throw new RuntimeException("Access denied.");
        }

        // Convert all Tag entities associated with the note
        // into TagResponse DTOs and return them
        return note.getTags()
                .stream()
                .map(this::mapTag)
                .toList();
    }

    /**
     * Retrieves all notes assigned to a specific tag.
     *
     * @param tagId ID of the tag.
     * @return List of notes associated with the tag.
     */
    public List<NoteSummaryResponse> getNotesByTag(Long tagId) {

        // Get current workspace
        Workspace workspace = getCurrentWorkspace();

        // Find tag
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found."));

        // Ensure tag belongs to current workspace
        if (!tag.getWorkspace().getId().equals(workspace.getId())) {
            throw new RuntimeException("Access denied.");
        }

        // Convert all notes associated with the tag
        // into NoteSummaryResponse DTOs
        return tag.getNotes()
                .stream()
                .map(note -> NoteSummaryResponse.builder()
                        .id(note.getId())
                        .title(note.getTitle())
                        .build())
                .toList();
    }
}