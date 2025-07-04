package edu.HanYi.dto.request;

import jakarta.validation.constraints.*;

public record FlashcardCreateRequest(
        @NotNull Integer userId,
        @NotBlank @Size(max = 25) String frontText,
        @NotBlank @Size(max = 35) String backText
) {}
