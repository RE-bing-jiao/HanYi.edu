package edu.HanYi.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record EntryCreateRequest(
        @NotNull Integer userId,
        @NotNull Integer courseId,
        @NotNull @FutureOrPresent LocalDateTime entryDate
) {}