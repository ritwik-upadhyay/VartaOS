package com.vartaos.vartaosbackend.service;

import com.vartaos.vartaosbackend.dto.folder.FolderStatisticsResponse;
import com.vartaos.vartaosbackend.entity.Folder;
import com.vartaos.vartaosbackend.entity.Progress;
import com.vartaos.vartaosbackend.entity.User;
import com.vartaos.vartaosbackend.entity.Workspace;
import com.vartaos.vartaosbackend.repository.FolderRepository;
import com.vartaos.vartaosbackend.repository.ProgressRepository;
import com.vartaos.vartaosbackend.repository.UserRepository;
import com.vartaos.vartaosbackend.repository.WorkspaceRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final FolderRepository folderRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final FolderStatisticsService folderStatisticsService;

    public ProgressService(ProgressRepository progressRepository,
                           FolderRepository folderRepository,
                           WorkspaceRepository workspaceRepository,
                           UserRepository userRepository,
                           FolderStatisticsService folderStatisticsService) {

        this.progressRepository = progressRepository;
        this.folderRepository = folderRepository;
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
        this.folderStatisticsService = folderStatisticsService;
    }

    public void markTopicComplete(Long folderId) {

        Workspace workspace = getCurrentWorkspace();

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found."));

        if (!folder.getWorkspace().getId().equals(workspace.getId())) {
            throw new RuntimeException("Access denied.");
        }

        validateLeafFolder(folder);

        // Already completed? Nothing to do.
        if (folder.getCompleted()) {
            return;
        }

        folder.setCompleted(true);

        folderRepository.save(folder);

        recalculateProgress(workspace, folder);

    }

    public void refreshWorkspaceProgress(Workspace workspace) {
        recalculateProgress(workspace, null);
    }

    private void validateLeafFolder(Folder folder) {

        if (!folder.getChildFolders().isEmpty()) {
            throw new RuntimeException(
                    "Only leaf folders can be marked as completed."
            );
        }

    }

    private void recalculateProgress(Workspace workspace,
                                     Folder lastCompletedFolder) {

        Progress progress = progressRepository.findByWorkspace(workspace)
                .orElseThrow(() -> new RuntimeException("Progress not found."));

        List<Folder> rootFolders =
                folderRepository.findByWorkspaceAndParentFolderIsNull(workspace);

        int totalTopics = 0;
        int completedTopics = 0;

        for (Folder rootFolder : rootFolders) {

            FolderStatisticsResponse statistics =
                    folderStatisticsService.calculateStatistics(rootFolder);

            totalTopics += statistics.getTotalTopics();
            completedTopics += statistics.getCompletedTopics();
        }

        int remainingTopics = totalTopics - completedTopics;

        double completionPercentage =
                totalTopics == 0
                        ? 0.0
                        : ((double) completedTopics / totalTopics) * 100;

        completionPercentage =
                Math.round(completionPercentage * 100.0) / 100.0;

        progress.setTotalTopics(totalTopics);
        progress.setCompletedTopics(completedTopics);
        progress.setRemainingTopics(remainingTopics);
        progress.setCompletionPercentage(completionPercentage);

        progress.setLastCompletedFolder(lastCompletedFolder);
        progress.setLastCompletedDate(LocalDateTime.now());

        progressRepository.save(progress);
    }

    private Workspace getCurrentWorkspace() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        return workspaceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Workspace not found."));
    }

}
