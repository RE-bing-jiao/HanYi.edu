package edu.HanYi.dto.response;

import java.time.LocalDateTime;

public record FlashcardResponse(
        Integer id,
        Integer userId,
        String username,
        String frontText,
        String backText,
        LocalDateTime createdAt
) {}
