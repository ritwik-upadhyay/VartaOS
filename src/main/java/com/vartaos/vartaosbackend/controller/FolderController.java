package com.vartaos.vartaosbackend.controller;

import com.vartaos.vartaosbackend.dto.folder.CreateFolderRequest;
import com.vartaos.vartaosbackend.dto.folder.FolderResponse;
import com.vartaos.vartaosbackend.service.FolderService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.vartaos.vartaosbackend.dto.folder.RenameFolderRequest;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

/**
 * REST controller responsible for folder-related APIs.
 */
@RestController
@RequestMapping("/api/folders")
public class FolderController {

    private final FolderService folderService;

    /**
     * Constructor injection for FolderService.
     */
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    /**
     * Creates a new folder inside the authenticated user's workspace.
     *
     * If parentFolderId is null, a root folder is created.
     * Otherwise, the folder is created inside the specified parent folder.
     *
     * @param authentication Authenticated user provided by Spring Security.
     * @param request Folder creation request.
     * @return Newly created folder.
     */
    @PostMapping
    public FolderResponse createFolder(
            Authentication authentication,
            @Valid @RequestBody CreateFolderRequest request) {

        return folderService.createFolder(
                authentication.getName(),
                request
        );
    }

    /**
     * Renames an existing folder.
     *
     * @param folderId ID of the folder to rename.
     * @param request Request containing the new folder name.
     * @return Updated folder information.
     */
    @PutMapping("/{folderId}")
    public FolderResponse renameFolder(
            @PathVariable Long folderId,
            @Valid @RequestBody RenameFolderRequest request) {

        return folderService.renameFolder(folderId, request);
    }

    /**
     * Returns the complete folder hierarchy
     * for the authenticated user's workspace.
     *
     * @param authentication Authenticated user.
     * @return Folder tree.
     */
    @GetMapping
    public List<FolderResponse> getFolderTree(Authentication authentication) {

        return folderService.getFolderTree(authentication.getName());
    }

    /**
     * Deletes a folder.
     *
     * @param folderId ID of the folder to delete.
     */
    @DeleteMapping("/{folderId}")
    public void deleteFolder(@PathVariable Long folderId) {

        folderService.deleteFolder(folderId);
    }
}