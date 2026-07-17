package com.vartaos.vartaosbackend.controller;

import com.vartaos.vartaosbackend.dto.note.NoteSummaryResponse;
import com.vartaos.vartaosbackend.dto.tag.CreateTagRequest;
import com.vartaos.vartaosbackend.dto.tag.TagResponse;
import com.vartaos.vartaosbackend.dto.tag.UpdateTagRequest;
import com.vartaos.vartaosbackend.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public TagResponse createTag(@Valid @RequestBody CreateTagRequest request) {
        return tagService.createTag(request);
    }

    @GetMapping
    public List<TagResponse> getTags() {
        return tagService.getTags();
    }

    @PutMapping("/{tagId}")
    public TagResponse updateTag(@PathVariable Long tagId,
                                 @Valid @RequestBody UpdateTagRequest request) {
        return tagService.updateTag(tagId, request);
    }

    @DeleteMapping("/{tagId}")
    public void deleteTag(@PathVariable Long tagId) {
        tagService.deleteTag(tagId);
    }

    /**
     * Retrieves all notes associated with a specific tag.
     *
     * @param tagId ID of the tag.
     * @return List of notes associated with the tag.
     */
    @GetMapping("/{tagId}/notes")
    public List<NoteSummaryResponse> getNotesByTag(@PathVariable Long tagId) {
        return tagService.getNotesByTag(tagId);
    }
}