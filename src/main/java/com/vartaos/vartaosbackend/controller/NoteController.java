package com.vartaos.vartaosbackend.controller;

import com.vartaos.vartaosbackend.dto.note.CreateNoteRequest;
import com.vartaos.vartaosbackend.dto.note.NoteResponse;
import com.vartaos.vartaosbackend.dto.note.UpdateNoteRequest;
import com.vartaos.vartaosbackend.dto.note.MoveNoteRequest;
import com.vartaos.vartaosbackend.dto.tag.TagResponse;
import com.vartaos.vartaosbackend.service.NoteService;
import com.vartaos.vartaosbackend.service.TagService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import com.vartaos.vartaosbackend.dto.note.NoteDisplayOrderRequest;

/**
 * REST controller responsible for note-related APIs.
 */
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final TagService tagService;

    /**
     * Constructor injection for NoteService.
     */
    public NoteController(NoteService noteService,
                          TagService tagService) {
        this.noteService = noteService;
        this.tagService = tagService;
    }

    /**
     * Creates a new note.
     *
     * @param request Request containing note details.
     * @return Created note.
     */
    @PostMapping
    public NoteResponse createNote(
            @Valid @RequestBody CreateNoteRequest request) {

        return noteService.createNote(request);
    }

    /**
     * Retrieves a note by its ID.
     *
     * @param id Note ID.
     * @return Note details.
     */
    @GetMapping("/{id}")
    public NoteResponse getNote(@PathVariable Long id) {

        return noteService.getNote(id);
    }

    /**
     * Updates an existing note.
     *
     * @param id      Note ID.
     * @param request Updated note details.
     * @return Updated note.
     */
    @PutMapping("/{id}")
    public NoteResponse updateNote(@PathVariable Long id,
                                   @Valid @RequestBody UpdateNoteRequest request) {

        return noteService.updateNote(id, request);
    }

    /**
     * Deletes a note by its ID.
     *
     * @param id Note ID.
     */
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {

        noteService.deleteNote(id);
    }

    /**
     * Moves a note to another folder.
     *
     * @param id      ID of the note to move.
     * @param request Request containing the destination folder ID.
     * @return Updated note information.
     */
    @PutMapping("/{id}/move")
    public NoteResponse moveNote(@PathVariable Long id,
                                 @RequestBody MoveNoteRequest request) {

        return noteService.moveNote(id, request);
    }

    /**
     * Updates the display order of multiple notes.
     *
     * @param requests List containing note IDs and their new display order.
     */
    @PutMapping("/display-order")
    public ResponseEntity<Void> updateDisplayOrder(
            @RequestBody List<NoteDisplayOrderRequest> requests) {

        noteService.updateDisplayOrder(requests);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{noteId}/tags/{tagId}")
    public void assignTagToNote(@PathVariable Long noteId,
                                @PathVariable Long tagId) {

        tagService.assignTagToNote(noteId, tagId);
    }

    @DeleteMapping("/{noteId}/tags/{tagId}")
    public void removeTagFromNote(@PathVariable Long noteId,
                                  @PathVariable Long tagId) {

        tagService.removeTagFromNote(noteId, tagId);
    }

    /**
     * Retrieves all tags assigned to a specific note.
     *
     * @param noteId ID of the note.
     * @return List of tags assigned to the note.
     */
    @GetMapping("/{noteId}/tags")
    public List<TagResponse> getTagsOfNote(@PathVariable Long noteId) {
        return tagService.getTagsOfNote(noteId);
    }
}