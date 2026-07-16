package com.vartaos.vartaosbackend.entity;

import com.vartaos.vartaosbackend.entity.enums.NoteType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a note inside a folder.
 *
 * A note stores study material, interview notes,
 * project documentation, or any other user-created content.
 */
@Entity
@Table(name = "notes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    /**
     * Primary key for the note.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title of the note.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Main content of the note.
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * Category of the note.
     *
     * Used to classify notes such as learning notes,
     * revision notes, interview questions, etc.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private NoteType type = NoteType.GENERAL;

    /**
     * Controls the display order inside a folder.
     */
    private Integer displayOrder;

    /**
     * Timestamp when the note was created.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the note was last updated.
     */
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Folder that contains this note.
     *
     * Many notes can belong to one folder.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;
}