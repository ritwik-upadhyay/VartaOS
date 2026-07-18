package com.vartaos.vartaosbackend.dto.folder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the calculated study statistics
 * for a folder.
 *
 * These statistics are computed recursively
 * from all descendant leaf folders and are
 * not stored in the database.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderStatisticsResponse {

    /**
     * Total number of leaf study topics.
     */
    private Integer totalTopics;

    /**
     * Number of completed leaf study topics.
     */
    private Integer completedTopics;

    /**
     * Number of remaining leaf study topics.
     */
    private Integer remainingTopics;

    /**
     * Overall completion percentage.
     */
    private Double completionPercentage;

}