package com.vartaos.vartaosbackend.entity.enums;

/**
 * Represents the category of a note.
 *
 * Note types help classify notes for better
 * organization, filtering, and future AI workflows.
 */
public enum NoteType {

    /**
     * General purpose note.
     */
    GENERAL,

    /**
     * Detailed learning notes.
     */
    LEARNING,

    /**
     * Concise revision notes.
     */
    REVISION,

    /**
     * Interview preparation questions.
     */
    INTERVIEW,

    /**
     * Mistakes and learnings.
     */
    MISTAKES

}