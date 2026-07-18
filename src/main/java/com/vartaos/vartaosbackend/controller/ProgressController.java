package com.vartaos.vartaosbackend.controller;

import com.vartaos.vartaosbackend.service.ProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @PatchMapping("/complete/{folderId}")
    public ResponseEntity<Void> markTopicComplete(
            @PathVariable Long folderId) {

        progressService.markTopicComplete(folderId);

        return ResponseEntity.ok().build();
    }
}