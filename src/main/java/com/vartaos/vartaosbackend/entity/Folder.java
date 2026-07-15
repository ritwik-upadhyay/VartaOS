package com.vartaos.vartaosbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a folder inside a workspace.
 *
 * A folder can contain child folders, allowing
 * unlimited levels of nesting similar to Obsidian.
 */
@Entity
@Table(name = "folders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Folder {

    /**
     * Unique identifier for the folder.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name displayed to the user.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Determines the display order of folders
     * within the same parent folder.
     */
    @Column(nullable = false)
    private Integer displayOrder;

    /**
     * Timestamp when the folder was created.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp of the most recent update.
     */
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Workspace that owns this folder.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    /**
     * Parent folder.
     * Null indicates a root-level folder.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_folder_id")
    private Folder parentFolder;

    /**
     * Child folders contained within this folder.
     */
    @OneToMany(mappedBy = "parentFolder")
    private List<Folder> childFolders;
}