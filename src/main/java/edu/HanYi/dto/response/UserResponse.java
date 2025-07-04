package edu.HanYi.dto.response;

import edu.HanYi.model.User.Role;

import java.util.List;

public record UserResponse(
        Integer id,
        String username,
        String email,
        Role role,
        List<FlashcardResponse> flashcards,
        List<EntryResponse> entries
) {
    public record FlashcardResponse(
            Integer id,
            String frontText,
            String backText
    ) {}

    public record EntryResponse(
            Integer id,
            Integer courseId,
            String courseHeader
    ) {}
}
