package com.vartaos.vartaosbackend.service;

import com.vartaos.vartaosbackend.dto.folder.CreateFolderRequest;
import com.vartaos.vartaosbackend.dto.folder.FolderResponse;
import com.vartaos.vartaosbackend.dto.folder.MoveFolderRequest;
import com.vartaos.vartaosbackend.entity.Folder;
import com.vartaos.vartaosbackend.entity.User;
import com.vartaos.vartaosbackend.entity.Workspace;
import com.vartaos.vartaosbackend.exception.AuthenticationException;
import com.vartaos.vartaosbackend.repository.FolderRepository;
import com.vartaos.vartaosbackend.repository.UserRepository;
import com.vartaos.vartaosbackend.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import com.vartaos.vartaosbackend.dto.folder.RenameFolderRequest;
import com.vartaos.vartaosbackend.dto.folder.FolderDisplayOrderRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;

/**
 * Service responsible for folder-related business logic.
 */
@Service
public class FolderService {

    private final FolderRepository folderRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final ProgressService progressService;

    public FolderService(FolderRepository folderRepository,
                         WorkspaceRepository workspaceRepository,
                         UserRepository userRepository,
                         ProgressService progressService) {
        this.folderRepository = folderRepository;
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
        this.progressService = progressService;
    }

    /**
     * Creates a new root-level folder inside the user's workspace.
     */
    public FolderResponse createFolder(String email,
                                       CreateFolderRequest request) {

        // Find authenticated user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found."));

        // Find user's workspace
        Workspace workspace = workspaceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Workspace not found."));

        // Parent folder (null means root folder)
        Folder parentFolder = null;

        //Check if a parent folder ID was provided
        if (request.getParentFolderId() != null) {

            // Find the parent folder
            parentFolder = folderRepository.findById(request.getParentFolderId())
                    .orElseThrow(() -> new RuntimeException("Parent folder not found."));
        }

        // Create folder
        Folder folder = Folder.builder()
                .name(request.getName())
                .displayOrder(0)
                .workspace(workspace)
                .parentFolder(parentFolder)
                .build();

        // Save folder
        folderRepository.save(folder);

        progressService.refreshWorkspaceProgress(workspace);

        // Return response
        return FolderResponse.builder()
                .id(folder.getId())
                .name(folder.getName())
                .parentFolderId(
                        parentFolder != null ? parentFolder.getId() : null
                )
                .completed(folder.getCompleted())
                .build();
    }

    /**
     * Renames an existing folder.
     *
     * @param folderId ID of the folder to rename.
     * @param request Request containing the new folder name.
     * @return Updated folder information.
     */
    public FolderResponse renameFolder(Long folderId,
                                       RenameFolderRequest request) {

        // Find folder by ID
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found."));

        // Update folder name
        folder.setName(request.getName());

        // Save updated folder
        folderRepository.save(folder);

        // Return updated folder
        return FolderResponse.builder()
                .id(folder.getId())
                .name(folder.getName())
                .parentFolderId(
                        folder.getParentFolder() != null
                                ? folder.getParentFolder().getId()
                                : null
                )
                .completed(folder.getCompleted())
                .build();
    }

    /**
     * Deletes a folder by its ID.
     *
     * @param folderId ID of the folder to delete.
     */
    public void deleteFolder(Long folderId) {

        // Find folder
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found."));

        // Delete folder
        folderRepository.delete(folder);
    }

    /**
     * Returns the complete folder hierarchy for the authenticated user's workspace.
     *
     * @param email Email of the authenticated user.
     * @return List of root folders with all nested child folders.
     */
    public List<FolderResponse> getFolderTree(String email) {

        // Find authenticated user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found."));

        // Find user's workspace
        Workspace workspace = workspaceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Workspace not found."));

        // Get all root folders
        List<Folder> rootFolders =
                folderRepository.findByWorkspaceAndParentFolderIsNull(workspace);

        // Convert each root folder into a tree
        return rootFolders.stream()
                .map(this::buildFolderTree)
                .collect(Collectors.toList());
    }

    /**
     * Recursively converts a Folder entity into a FolderResponse DTO,
     * including all of its child folders.
     *
     * @param folder Folder entity to convert.
     * @return FolderResponse containing nested child folders.
     */
    private FolderResponse buildFolderTree(Folder folder) {

        // Convert all child folders recursively
        List<FolderResponse> children = folder.getChildFolders()
                .stream()
                .map(this::buildFolderTree)
                .collect(Collectors.toList());

        // Return current folder with its children
        return FolderResponse.builder()
                .id(folder.getId())
                .name(folder.getName())
                .parentFolderId(
                        folder.getParentFolder() != null
                                ? folder.getParentFolder().getId()
                                : null
                )
                .completed(folder.getCompleted())
                .children(children)
                .build();
    }

    /**
     * Moves a folder to another parent folder.
     *
     * @param id      ID of the folder to move.
     * @param request Request containing the new parent folder ID.
     * @return Updated folder information.
     */
    public FolderResponse moveFolder(Long id,
                                     MoveFolderRequest request) {

        // Find the folder to move
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found."));

        // New parent folder (can be null for a root folder)
        Folder newParent = null;

        if (request.getParentFolderId() != null) {
            newParent = folderRepository.findById(request.getParentFolderId())
                    .orElseThrow(() -> new RuntimeException("Parent folder not found."));
        }

        // Update the parent folder
        folder.setParentFolder(newParent);

        // Save changes
        folderRepository.save(folder);

        // Return updated folder
        return FolderResponse.builder()
                .id(folder.getId())
                .name(folder.getName())
                .parentFolderId(
                        newParent != null ? newParent.getId() : null
                )
                .completed(folder.getCompleted())
                .children(new ArrayList<>())
                .build();
    }

    /**
     * Updates the display order of multiple folders.
     *
     * @param requests List containing folder IDs and their new display order.
     */
    public void updateDisplayOrder(List<FolderDisplayOrderRequest> requests) {

        for (FolderDisplayOrderRequest request : requests) {

            // Find folder
            Folder folder = folderRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Folder not found."));

            // Update display order
            folder.setDisplayOrder(request.getDisplayOrder());

            // Save updated folder
            folderRepository.save(folder);
        }
    }
}
