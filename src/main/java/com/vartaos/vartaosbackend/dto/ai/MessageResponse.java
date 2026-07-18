package com.vartaos.vartaosbackend.dto.ai;

import com.vartaos.vartaosbackend.entity.enums.MessageSender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageResponse {

    private MessageSender sender;

    private String content;

    private LocalDateTime createdAt;
}