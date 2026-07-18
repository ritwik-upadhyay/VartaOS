package com.vartaos.vartaosbackend.service;

import com.vartaos.vartaosbackend.repository.FolderRepository;
import com.vartaos.vartaosbackend.dto.folder.FolderStatisticsResponse;
import com.vartaos.vartaosbackend.entity.Folder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for calculating folder-level
 * study statistics.
 *
 * This service recursively analyzes the folder hierarchy
 * and computes:
 * - Total study topics
 * - Completed topics
 * - Remaining topics
 * - Completion percentage
 *
 * The calculated statistics are used by:
 * - Progress Module
 * - Dashboard
 * - AI Assistant
 * - Analytics
 */
@Service
public class FolderStatisticsService {

    /**
     * Calculates study statistics for the given folder.
     *
     * Statistics are calculated recursively by traversing
     * all descendant leaf folders.
     *
     * @param folder Folder whose statistics are to be calculated.
     * @return Calculated folder statistics.
     */
    public FolderStatisticsResponse calculateStatistics(Folder folder) {

        if (folder.getChildFolders() == null || folder.getChildFolders().isEmpty()) {

            int completedTopics = folder.getCompleted() ? 1 : 0;

            return FolderStatisticsResponse.builder()
                    .totalTopics(1)
                    .completedTopics(completedTopics)
                    .remainingTopics(1 - completedTopics)
                    .completionPercentage(completedTopics == 1 ? 100.0 : 0.0)
                    .build();
        }

        int totalTopics = 0;
        int completedTopics = 0;

        for (Folder child : folder.getChildFolders()) {

            FolderStatisticsResponse childStatistics =
                    calculateStatistics(child);

            totalTopics += childStatistics.getTotalTopics();
            completedTopics += childStatistics.getCompletedTopics();

        }

        int remainingTopics = totalTopics - completedTopics;

        double completionPercentage =
                totalTopics == 0
                        ? 0.0
                        : ((double) completedTopics / totalTopics) * 100;

        completionPercentage =
                Math.round(completionPercentage * 100.0) / 100.0;

        return FolderStatisticsResponse.builder()
                .totalTopics(totalTopics)
                .completedTopics(completedTopics)
                .remainingTopics(remainingTopics)
                .completionPercentage(completionPercentage)
                .build();

    }
}