package com.vartaos.vartaosbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a tag that can be assigned
 * to one or more notes.
 *
 * Tags help organize notes and improve
 * searching and filtering.
 */
@Entity
@Table(name = "tags")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    /**
     * Unique identifier for the tag.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name displayed to the user.
     *
     * Examples:
     * Java
     * Spring Boot
     * Interview
     * Revision
     */
    @Column(nullable = false)
    private String name;

    /**
     * Timestamp when the tag was created.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the tag was last updated.
     */
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Workspace that owns this tag.
     *
     * Tags are unique within a workspace
     * and are not shared across users.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    /**
     * Notes associated with this tag.
     *
     * A tag can belong to multiple notes.
     */
    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private List<Note> notes = new ArrayList<>();

}