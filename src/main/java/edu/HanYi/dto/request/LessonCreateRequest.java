package edu.HanYi.dto.request;

import jakarta.validation.constraints.*;

public record LessonCreateRequest(
        @NotNull Integer lessonOrderNum,
        @NotBlank @Size(max = 25) String header,
        @NotBlank @Size(max = 125) String description,
        @NotBlank @Size(max = 125) String url,
        @NotNull Integer courseId
) {}