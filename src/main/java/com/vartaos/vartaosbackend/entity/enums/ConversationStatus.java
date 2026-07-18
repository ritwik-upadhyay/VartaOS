package com.vartaos.vartaosbackend.entity.enums;

/**
 * Represents the current lifecycle state of an AI conversation.
 *
 * <p>ACTIVE:
 * The student is currently learning and the conversation is ongoing.
 *
 * <p>COMPLETED:
 * The learning session has been completed for the current topic.
 *
 * <p>ARCHIVED:
 * The conversation is preserved for future reference but is no longer active.
 *
 * @author Ritwik
 * @since VartaOS v1
 */
public enum ConversationStatus {

    /**
     * Conversation is currently active.
     */
    ACTIVE,

    /**
     * Learning for this conversation has been completed.
     */
    COMPLETED,

    /**
     * Conversation has been archived.
     */
    ARCHIVED
}