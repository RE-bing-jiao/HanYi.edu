package edu.HanYi.dto.request;

import edu.HanYi.model.User.Role;
import jakarta.validation.constraints.*;

public record UserCreateRequest(
        @NotBlank @Size(min = 5, max = 25) String username,
        @NotBlank @Size(min = 8) String password,
        @NotBlank @Size(max = 35) @Email String email,
        @NotNull Role role
) {}