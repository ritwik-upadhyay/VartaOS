package com.vartaos.vartaosbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing the overall preparation
 * progress of a workspace.
 *
 * Stores aggregated study statistics such as
 * completed topics, remaining topics, completion
 * percentage, and the last completed study topic.
 */
@Entity
@Table(name = "progress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Progress {

    /**
     * Unique identifier for the progress tracker.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Workspace that owns this progress tracker.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false, unique = true)
    private Workspace workspace;

    /**
     * Total number of study topics.
     */
    @Builder.Default
    private Integer totalTopics = 0;

    /**
     * Number of completed study topics.
     */
    @Builder.Default
    private Integer completedTopics = 0;

    /**
     * Number of remaining study topics.
     */
    @Builder.Default
    private Integer remainingTopics = 0;

    /**
     * Overall completion percentage.
     */
    @Builder.Default
    private Double completionPercentage = 0.0;

    /**
     * Most recently completed study topic.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_completed_folder_id")
    private Folder lastCompletedFolder;

    /**
     * Date and time when the last topic
     * was completed.
     */
    private LocalDateTime lastCompletedDate;

    /**
     * Timestamp when this progress tracker
     * was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when this progress tracker
     * was last updated.
     */
    private LocalDateTime updatedAt;

    /**
     * Sets timestamps before the entity
     * is initially persisted.
     */
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the modification timestamp
     * before every update.
     */
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}