package edu.HanYi.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CourseCreateRequest(
        @NotBlank @Size(max = 25) String header,
        @NotBlank @Size(max = 125) String description,
        @NotNull Integer categoryId,
        @NotNull @FutureOrPresent LocalDateTime entryDate,
        @NotNull @FutureOrPresent LocalDateTime exitDate,
        @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal progress
) {}