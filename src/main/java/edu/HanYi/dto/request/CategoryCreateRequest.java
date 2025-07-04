package edu.HanYi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreateRequest(
        @NotBlank @Size(max = 25) String name,
        @NotBlank @Size(max = 125) String description
) {}